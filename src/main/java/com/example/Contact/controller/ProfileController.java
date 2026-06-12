
package com.example.Contact.controller;

import com.example.Contact.dto.PasswordChangeRequest;
import com.example.Contact.dto.ProfileRequest;
import com.example.Contact.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(profileService.getProfile(userDetails.getUsername()));
    }

    @PutMapping("/name")
    public ResponseEntity<Map<String, String>> updateName(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProfileRequest req) {
        return ResponseEntity.ok(profileService.updateName(userDetails.getUsername(), req));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PasswordChangeRequest req) {
        profileService.changePassword(userDetails.getUsername(), req);
        return ResponseEntity.ok().build();
    }
}