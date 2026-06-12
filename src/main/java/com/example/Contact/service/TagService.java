package com.example.Contact.service;

import com.example.Contact.dto.TagRequest;
import com.example.Contact.dto.TagResponse;
import com.example.Contact.model.Tag;
import com.example.Contact.model.User;
import com.example.Contact.repository.TagRepository;
import com.example.Contact.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public TagService(TagRepository tagRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    public List<TagResponse> getAll(String email) {
        User user = findUser(email);
        return tagRepository.findByUserId(user.getId())
                .stream().map(t -> new TagResponse(t.getId(), t.getName()))
                .collect(Collectors.toList());
    }

    public TagResponse create(TagRequest req, String email) {
        User user = findUser(email);
        if (tagRepository.existsByNameAndUserId(req.getName(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag already exists");
        }
        Tag tag = new Tag();
        tag.setName(req.getName());
        tag.setUser(user);
        Tag saved = tagRepository.save(tag);
        return new TagResponse(saved.getId(), saved.getName());
    }

    public void delete(Long tagId, String email) {
        User user = findUser(email);
        Tag tag = tagRepository.findByIdAndUserId(tagId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tagRepository.delete(tag);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}