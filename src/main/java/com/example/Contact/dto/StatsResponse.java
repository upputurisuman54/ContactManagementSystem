package com.example.Contact.dto;

import java.util.List;
import java.util.Map;

public class StatsResponse {
    private long totalContacts;
    private long favouriteCount;
    private long tagCount;
    private List<Map<String, Object>> topCompanies;
    private List<Map<String, Object>> contactsPerTag;

    public long getTotalContacts() { return totalContacts; }
    public void setTotalContacts(long totalContacts) { this.totalContacts = totalContacts; }

    public long getFavouriteCount() { return favouriteCount; }
    public void setFavouriteCount(long favouriteCount) { this.favouriteCount = favouriteCount; }

    public long getTagCount() { return tagCount; }
    public void setTagCount(long tagCount) { this.tagCount = tagCount; }

    public List<Map<String, Object>> getTopCompanies() { return topCompanies; }
    public void setTopCompanies(List<Map<String, Object>> topCompanies) { this.topCompanies = topCompanies; }

    public List<Map<String, Object>> getContactsPerTag() { return contactsPerTag; }
    public void setContactsPerTag(List<Map<String, Object>> contactsPerTag) { this.contactsPerTag = contactsPerTag; }
}