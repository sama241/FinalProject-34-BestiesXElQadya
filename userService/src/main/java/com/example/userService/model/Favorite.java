package com.example.userService.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generate UUID for Favorite ID
    private UUID id;
    private UUID userId;
    private String workerId;
    // Constructor

    // tab w id el record?
    public Favorite(UUID userId, String workerId, UUID id) {
        this.id = id;
        this.userId = userId;
        this.workerId = workerId;
    }

    public Favorite() {

    }

    public Favorite(UUID userId, String workerId ){
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.workerId = workerId;
    }


    public UUID getUserId() {
        return userId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public UUID getId() {
        return id;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    // Getters and Setters
    //favourite repo with crud  functions
    //user service includes fav repo
    //user


}