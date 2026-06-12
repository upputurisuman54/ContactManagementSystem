package com.example.Contact.dto;

public class TagResponse {
    private Long id;
    private String name;

    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}