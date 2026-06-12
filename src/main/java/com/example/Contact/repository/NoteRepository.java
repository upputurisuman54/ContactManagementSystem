package com.example.Contact.repository;

import com.example.Contact.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByContactIdOrderByCreatedAtDesc(Long contactId);
    Optional<Note> findByIdAndContactId(Long id, Long contactId);
}