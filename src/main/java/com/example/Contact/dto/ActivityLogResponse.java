package com.example.Contact.dto;

import com.example.Contact.model.ActivityAction;
import java.time.LocalDateTime;

public class ActivityLogResponse {

    private Long id;
    private ActivityAction action;
    private String contactName;
    private Long contactId;
    private LocalDateTime timestamp;

    public ActivityLogResponse(Long id, ActivityAction action, String contactName, Long contactId, LocalDateTime timestamp) {
        this.id = id;
        this.action = action;
        this.contactName = contactName;
        this.contactId = contactId;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public ActivityAction getAction() { return action; }
    public String getContactName() { return contactName; }
    public Long getContactId() { return contactId; }
    public LocalDateTime getTimestamp() { return timestamp; }
}