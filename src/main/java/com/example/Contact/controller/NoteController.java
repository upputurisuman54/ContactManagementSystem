package com.example.Contact.controller;

import com.example.Contact.dto.NoteRequest;
import com.example.Contact.dto.NoteResponse;
import com.example.Contact.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts/{contactId}/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<NoteResponse> getNotes(@PathVariable Long contactId, @AuthenticationPrincipal UserDetails user) {
        return noteService.getNotes(contactId, user.getUsername());
    }

    @PostMapping
    public ResponseEntity<NoteResponse> addNote(@PathVariable Long contactId, @RequestBody NoteRequest req,
                                                 @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.addNote(contactId, req, user.getUsername()));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long contactId, @PathVariable Long noteId,
                                            @AuthenticationPrincipal UserDetails user) {
        noteService.deleteNote(contactId, noteId, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}