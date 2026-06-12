package com.example.Contact.service;

import com.example.Contact.dto.PasswordChangeRequest;
import com.example.Contact.dto.ProfileRequest;
import com.example.Contact.model.User;
import com.example.Contact.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, String> getProfile(String email) {
        User user = findUser(email);
        return Map.of("name", user.getName(), "email", user.getEmail());
    }

    public Map<String, String> updateName(String email, ProfileRequest req) {
        User user = findUser(email);
        user.setName(req.getName());
        userRepository.save(user);
        return Map.of("name", user.getName());
    }

    public void changePassword(String email, PasswordChangeRequest req) {
        User user = findUser(email);
        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}