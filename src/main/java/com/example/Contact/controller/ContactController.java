package com.example.Contact.controller;

import com.example.Contact.dto.ContactRequest;
import com.example.Contact.dto.ContactResponse;
import com.example.Contact.dto.StatsResponse;
import com.example.Contact.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Page<ContactResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long tagId,
            @AuthenticationPrincipal UserDetails user) {
        return contactService.getAllContacts(user.getUsername(), page, size, tagId);
    }

    @GetMapping("/{id}")
    public ContactResponse getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return contactService.getById(id, user.getUsername());
    }

    @PostMapping
    public ResponseEntity<ContactResponse> create(@RequestBody ContactRequest req,
                                                   @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.create(req, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ContactResponse update(@PathVariable Long id, @RequestBody ContactRequest req,
                                   @AuthenticationPrincipal UserDetails user) {
        return contactService.update(id, req, user.getUsername());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        contactService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favourites")
    public List<ContactResponse> getFavourites(@AuthenticationPrincipal UserDetails user) {
        return contactService.getFavourites(user.getUsername());
    }

    @GetMapping("/search")
    public List<ContactResponse> search(@RequestParam String keyword,
                                         @AuthenticationPrincipal UserDetails user) {
        return contactService.search(keyword, user.getUsername());
    }

    @PostMapping("/{id}/photo")
    public ContactResponse uploadPhoto(@PathVariable Long id,
                                        @RequestParam("file") MultipartFile file,
                                        @AuthenticationPrincipal UserDetails user) throws IOException {
        return contactService.uploadPhoto(id, file, user.getUsername());
    }

    @PutMapping("/{id}/tags")
    public ContactResponse assignTags(@PathVariable Long id, @RequestBody List<Long> tagIds,
                                       @AuthenticationPrincipal UserDetails user) {
        return contactService.assignTags(id, tagIds, user.getUsername());
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv(@AuthenticationPrincipal UserDetails user) {
        String csv = contactService.exportCsv(user.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "contacts.csv");
        return ResponseEntity.ok().headers(headers).body(csv.getBytes());
    }

    @GetMapping("/stats")
    public StatsResponse getStats(@AuthenticationPrincipal UserDetails user) {
        return contactService.getStats(user.getUsername());
    }
}