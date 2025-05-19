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
    private boolean isAvailable;
    private List<String> skills;           // Dynamic list of skills
    private List<Integer> availableHours;  // Available working hours (e.g., [9, 10, 11, 14])
    private String location;
    private List<String> badges;            // For badges/certifications
    private double rating;

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    private double averageRating;
    // Constructors
    public Worker() {
        this.skills = new ArrayList<>();
        this.badges = new ArrayList<>();
        this.availableHours = new ArrayList<>();
        this.isAvailable = false;
        this.rating=0;
        this.isAvailable = false;  // ➔ Default to false when empty
        this.location= "";
        this.averageRating=0;

    }
    public Worker(String name, String email, String password, String location , String profession, List<String> skills, List<Integer> availableHours) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profession = profession;
        this.location = location;
        this.skills = skills;
        this.availableHours = availableHours;
        this.badges = new ArrayList<>();
        this.isAvailable = (availableHours != null && !availableHours.isEmpty());
        this.averageRating=0;// ➔ if availableHours not empty, available!
    }
   public Worker(String name, String email, String password, String profession, List<String> skills, List<Integer> availableHours) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profession = profession;
        this.skills = skills;
        this.availableHours = availableHours;
        this.badges = new ArrayList<>();
        this.isAvailable = (availableHours != null && !availableHours.isEmpty());
        this.averageRating=0;// ➔ if availableHours not empty, available!

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
        this.isAvailable = (availableHours != null && !availableHours.isEmpty());
    }

    public List<String> getBadges() {
        return badges;
    }

    public void setBadges(List<String> badges) {
        this.badges = badges;
    }


    public boolean bookHour(int hour) {
        if (availableHours.contains(hour)) {
            availableHours.remove(Integer.valueOf(hour));
            this.isAvailable = !availableHours.isEmpty(); // update availability
            return true;
        }
        return false;
    }

    // Method to check availability
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Method to add a badge
    public void addBadge(String badge) {
        if (!this.badges.contains(badge)) {
            this.badges.add(badge);
        }
    }
    public boolean addAvailableHour(int hour) {
        if (!availableHours.contains(hour)) {
            availableHours.add(hour);
            this.isAvailable = true;
            return true;
        }
        return false;
    }

    public boolean removeAvailableHour(int hour) {
        if (availableHours.contains(hour)) {
            availableHours.remove(Integer.valueOf(hour));
            this.isAvailable = !availableHours.isEmpty();
            return true;
        }
        return false;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;

    }

}
