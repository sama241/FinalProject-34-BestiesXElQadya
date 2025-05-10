package com.example.userService.model;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Auto-generate UUID for User ID
    private UUID id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String address;

    public User(String username, UUID id, String name, String password, String email, String phone, String address) {
        this.username = username;
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }


    // tab wel id?

    public User(String username, String name, String password, String email, String phone, String address) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // tab wel id?

    public User(String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
    }



    public User() {
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public UUID getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}