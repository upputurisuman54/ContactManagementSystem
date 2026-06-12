package com.example.Contact.service;

import com.example.Contact.dto.ActivityLogResponse;
import com.example.Contact.model.ActivityAction;
import com.example.Contact.model.ActivityLog;
import com.example.Contact.model.Contact;
import com.example.Contact.model.User;
import com.example.Contact.repository.ActivityLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ActivityLogService {

    private final ActivityLogRepository repo;

    public ActivityLogService(ActivityLogRepository repo) {
        this.repo = repo;
    }

    public void log(User user, ActivityAction action, Contact contact) {
        ActivityLog entry = new ActivityLog();
        entry.setUser(user);
        entry.setAction(action);
        entry.setContactName(contact.getName());
        entry.setContactId(contact.getId());
        repo.save(entry);
    }

    public Page<ActivityLogResponse> getLog(User user, int page) {
        return repo.findByUserOrderByTimestampDesc(user, PageRequest.of(page, 20))
                .map(e -> new ActivityLogResponse(e.getId(), e.getAction(), e.getContactName(), e.getContactId(), e.getTimestamp()));
    }
}