package com.example.Contact.service;

import com.example.Contact.dto.NoteRequest;
import com.example.Contact.dto.NoteResponse;
import com.example.Contact.model.ActivityAction;
import com.example.Contact.model.Contact;
import com.example.Contact.model.Note;
import com.example.Contact.model.User;
import com.example.Contact.repository.ContactRepository;
import com.example.Contact.repository.NoteRepository;
import com.example.Contact.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public NoteService(NoteRepository noteRepository, ContactRepository contactRepository,
                       UserRepository userRepository, ActivityLogService activityLogService) {
        this.noteRepository = noteRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    public List<NoteResponse> getNotes(Long contactId, String email) {
        verifyContactOwnership(contactId, email);
        return noteRepository.findByContactIdOrderByCreatedAtDesc(contactId)
                .stream().map(n -> new NoteResponse(n.getId(), n.getContent(), n.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public NoteResponse addNote(Long contactId, NoteRequest req, String email) {
        User user = findUser(email);
        Contact contact = contactRepository.findByIdAndUserId(contactId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        Note note = new Note();
        note.setContent(req.getContent());
        note.setContact(contact);
        Note saved = noteRepository.save(note);
        activityLogService.log(user, ActivityAction.NOTE_ADDED, contact);
        return new NoteResponse(saved.getId(), saved.getContent(), saved.getCreatedAt());
    }

    public void deleteNote(Long contactId, Long noteId, String email) {
        verifyContactOwnership(contactId, email);
        Note note = noteRepository.findByIdAndContactId(noteId, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
        noteRepository.delete(note);
    }

    private Contact verifyContactOwnership(Long contactId, String email) {
        User user = findUser(email);
        return contactRepository.findByIdAndUserId(contactId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}