package com.example.workerService.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "workers")
public class Worker {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private String profession;
    private List<String> skills;
    private List<Integer> availableHours;
    private List<String> badges;

    // Constructors
    public Worker() {
        this.skills = new ArrayList<>();
        this.badges = new ArrayList<>();
        this.availableHours = new ArrayList<>();
    }

    public Worker(String name, String email, String password, String profession, List<String> skills, List<Integer> availableHours) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profession = profession;
        this.skills = skills;
        this.availableHours = availableHours;
        this.badges = new ArrayList<>();
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<Integer> getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(List<Integer> availableHours) {
        this.availableHours = availableHours;
    }

    public List<String> getBadges() {
        return badges;
    }

    public void setBadges(List<String> badges) {
        this.badges = badges;
    }



    // 🔥 Helper Methods

    // Method to book an available hour
    public boolean bookHour(int hour) {
        if (availableHours.contains(hour)) {
            availableHours.remove(Integer.valueOf(hour)); // Remove the booked hour
            return true; // Successful booking
        }
        return false; // Hour not available
    }

    // Method to check availability
    public boolean isAvailable() {
        return !availableHours.isEmpty();
    }

    // Method to add a badge
    public void addBadge(String badge) {
        if (!this.badges.contains(badge)) {
            this.badges.add(badge);
        }
    }




}
