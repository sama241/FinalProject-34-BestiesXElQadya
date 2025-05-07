package com.example.searchService.model;

public class SearchRequest {

    private String category;
    private String location;
    private Boolean available;  // <-- Notice it's Boolean not boolean

    public SearchRequest() {}

    public SearchRequest(String category, String location, Boolean available) {
        this.category = category;
        this.location = location;
        this.available = available;
    }

    // Getters and Setters

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
