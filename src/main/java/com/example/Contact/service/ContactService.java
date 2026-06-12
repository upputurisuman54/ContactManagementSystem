package com.example.Contact.service;

import com.example.Contact.dto.ContactRequest;
import com.example.Contact.dto.ContactResponse;
import com.example.Contact.dto.StatsResponse;
import com.example.Contact.dto.TagResponse;
import com.example.Contact.model.ActivityAction;
import com.example.Contact.model.Contact;
import com.example.Contact.model.Tag;
import com.example.Contact.model.User;
import com.example.Contact.repository.ContactRepository;
import com.example.Contact.repository.TagRepository;
import com.example.Contact.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ActivityLogService activityLogService;

    private static final String UPLOAD_DIR = "uploads/photos/";

    public ContactService(ContactRepository contactRepository, UserRepository userRepository,
                          TagRepository tagRepository, ActivityLogService activityLogService) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.activityLogService = activityLogService;
    }

    public Page<ContactResponse> getAllContacts(String email, int page, int size, Long tagId) {
        User user = findUser(email);
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Contact> contacts = tagId != null
                ? contactRepository.findByUserIdAndTagId(user.getId(), tagId, pageable)
                : contactRepository.findByUserId(user.getId(), pageable);
        return contacts.map(this::toResponse);
    }

    public ContactResponse getById(Long id, String email) {
        User user = findUser(email);
        Contact contact = contactRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        return toResponse(contact);
    }

    public ContactResponse create(ContactRequest req, String email) {
        User user = findUser(email);
        Contact contact = new Contact();
        applyFields(contact, req, user);
        Contact saved = contactRepository.save(contact);
        activityLogService.log(user, ActivityAction.CREATED, saved);
        return toResponse(saved);
    }

    public ContactResponse update(Long id, ContactRequest req, String email) {
        User user = findUser(email);
        Contact contact = contactRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        applyFields(contact, req, user);
        Contact updated = contactRepository.save(contact);
        activityLogService.log(user, ActivityAction.UPDATED, updated);
        return toResponse(updated);
    }

    public void delete(Long id, String email) {
        User user = findUser(email);
        Contact contact = contactRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        activityLogService.log(user, ActivityAction.DELETED, contact);
        contactRepository.delete(contact);
    }

    public List<ContactResponse> getFavourites(String email) {
        User user = findUser(email);
        return contactRepository.findByUserIdAndFavouriteTrue(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ContactResponse> search(String keyword, String email) {
        User user = findUser(email);
        return contactRepository.searchContacts(user.getId(), keyword)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ContactResponse uploadPhoto(Long id, MultipartFile file, String email) throws IOException {
        User user = findUser(email);
        Contact contact = contactRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + filename);
        Files.write(path, file.getBytes());

        contact.setPhotoUrl("/photos/" + filename);
        Contact saved = contactRepository.save(contact);
        activityLogService.log(user, ActivityAction.PHOTO_CHANGED, saved);
        return toResponse(saved);
    }

    public ContactResponse assignTags(Long id, List<Long> tagIds, String email) {
        User user = findUser(email);
        Contact contact = contactRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Set<Tag> tags = tagIds.stream()
                .map(tagId -> tagRepository.findByIdAndUserId(tagId, user.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found: " + tagId)))
                .collect(Collectors.toSet());

        contact.setTags(tags);
        Contact saved = contactRepository.save(contact);
        activityLogService.log(user, ActivityAction.TAGGED, saved);
        return toResponse(saved);
    }

    public String exportCsv(String email) {
        User user = findUser(email);
        List<Contact> contacts = contactRepository.findAllByUserId(user.getId());

        StringBuilder csv = new StringBuilder();
        csv.append("Name,Email,Phone,Company,Favourite,Tags\n");
        for (Contact c : contacts) {
            String tags = c.getTags().stream().map(Tag::getName).collect(Collectors.joining(";"));
            csv.append(escapeCsv(c.getName())).append(",")
               .append(escapeCsv(c.getEmail())).append(",")
               .append(escapeCsv(c.getPhone())).append(",")
               .append(escapeCsv(c.getCompany())).append(",")
               .append(c.isFavourite()).append(",")
               .append(escapeCsv(tags)).append("\n");
        }
        return csv.toString();
    }

    public StatsResponse getStats(String email) {
        User user = findUser(email);
        Long userId = user.getId();

        StatsResponse stats = new StatsResponse();
        stats.setTotalContacts(contactRepository.countByUserId(userId));
        stats.setFavouriteCount(contactRepository.countByUserIdAndFavouriteTrue(userId));
        stats.setTagCount(tagRepository.findByUserId(userId).size());

        List<Object[]> companyRows = contactRepository.topCompaniesByUser(userId, PageRequest.of(0, 5));
        List<Map<String, Object>> companies = companyRows.stream().map(row -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("company", row[0]);
            m.put("count", row[1]);
            return m;
        }).collect(Collectors.toList());
        stats.setTopCompanies(companies);

        List<Object[]> tagRows = contactRepository.contactsPerTagByUser(userId);
        List<Map<String, Object>> perTag = tagRows.stream().map(row -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("tag", row[0]);
            m.put("count", row[1]);
            return m;
        }).collect(Collectors.toList());
        stats.setContactsPerTag(perTag);

        return stats;
    }

    private void applyFields(Contact contact, ContactRequest req, User user) {
        contact.setName(req.getName());
        contact.setEmail(req.getEmail());
        contact.setPhone(req.getPhone());
        contact.setCompany(req.getCompany());
        contact.setFavourite(req.isFavourite());
        contact.setUser(user);
    }

    private ContactResponse toResponse(Contact c) {
        ContactResponse res = new ContactResponse();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setEmail(c.getEmail());
        res.setPhone(c.getPhone());
        res.setCompany(c.getCompany());
        res.setFavourite(c.isFavourite());
        res.setPhotoUrl(c.getPhotoUrl());
        res.setTags(c.getTags().stream()
                .map(t -> new TagResponse(t.getId(), t.getName()))
                .collect(Collectors.toList()));
        return res;
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}