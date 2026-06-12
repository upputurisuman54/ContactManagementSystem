package com.example.Contact.service;

import com.example.Contact.model.ActivityAction;
import com.example.Contact.model.Contact;
import com.example.Contact.model.User;
import com.example.Contact.repository.ContactRepository;
import com.example.Contact.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public ImportService(ContactRepository contactRepository, UserRepository userRepository,
                         ActivityLogService activityLogService) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    public int importCsv(MultipartFile file, String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        boolean firstLine = true;
        List<Contact> toSave = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            if (firstLine) { firstLine = false; continue; }
            if (line.trim().isEmpty()) continue;

            String[] cols = line.split(",", -1);
            if (cols.length < 4) continue;

            String email2 = cols[1].trim();
            if (contactRepository.existsByEmailAndUserId(email2, user.getId())) continue;

            Contact c = new Contact();
            c.setName(cols[0].trim());
            c.setEmail(email2);
            c.setPhone(cols[2].trim());
            c.setCompany(cols[3].trim());
            c.setFavourite(cols.length > 4 && cols[4].trim().equalsIgnoreCase("true"));
            c.setUser(user);
            toSave.add(c);
        }

        List<Contact> saved = contactRepository.saveAll(toSave);
        saved.forEach(c -> activityLogService.log(user, ActivityAction.CREATED, c));
        return saved.size();
    }
}