package com.example.userService.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String address;

    // Constructor with all fields
    public User(String username, String id, String name, String password, String email, String phone, String address) {
        this.username = username;
        this.id = id != null ? id : generateRandomId();
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Constructor without ID (ID is auto-generated)
    public User(String username, String name, String password, String email, String phone, String address) {
        this.id = generateRandomId();
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Constructor for username and password only
    public User(String username, String password) {
        this.id = generateRandomId();
        this.username = username;
        this.password = password;
    }

    // Default constructor
    public User() {
        this.id = generateRandomId();
    }

    // Generate random alphanumeric ID (12 characters)
    private String generateRandomId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    // Getters and Setters
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

    public String getId() {
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

    public void setId(String id) {
        this.id = id;
    }
}
