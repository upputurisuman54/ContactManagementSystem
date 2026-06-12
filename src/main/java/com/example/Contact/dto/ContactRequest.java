package com.example.Contact.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {
    private String name;
    private String email;
    private String phone;
    private String company;
    private boolean favourite;
}