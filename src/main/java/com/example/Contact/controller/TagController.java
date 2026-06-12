package com.example.Contact.controller;

import com.example.Contact.dto.TagRequest;
import com.example.Contact.dto.TagResponse;
import com.example.Contact.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagResponse> getAll(@AuthenticationPrincipal UserDetails user) {
        return tagService.getAll(user.getUsername());
    }

    @PostMapping
    public ResponseEntity<TagResponse> create(@RequestBody TagRequest req, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.create(req, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        tagService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}