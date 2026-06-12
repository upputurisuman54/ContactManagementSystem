package com.example.Contact.controller;

import com.example.Contact.dto.ActivityLogResponse;
import com.example.Contact.model.User;
import com.example.Contact.repository.UserRepository;
import com.example.Contact.service.ActivityLogService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity")
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    private final UserRepository userRepository;

    public ActivityLogController(ActivityLogService activityLogService, UserRepository userRepository) {
        this.activityLogService = activityLogService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Page<ActivityLogResponse>> getActivity(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(activityLogService.getLog(user, page));
    }
}