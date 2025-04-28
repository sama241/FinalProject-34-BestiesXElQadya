package com.example.userService.controller;

import com.example.userService.model.Favorite;
import com.example.userService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.userService.service.UserService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    // get all
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    // Create a new User
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    // Get User by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Update User details
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete User by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }


    // Favorite Worker Functions

    // Add Worker to Favorites
    @PostMapping("/{userId}/favorites")
    public ResponseEntity<Void> addFavoriteWorker(@PathVariable UUID userId, @RequestParam UUID workerId) {
        userService.addFavoriteWorker(userId, workerId);
        return ResponseEntity.ok().build();
    }

    // Remove Worker from Favorites
    @DeleteMapping("/{userId}/favorites")
    public ResponseEntity<Void> removeFavoriteWorker(@PathVariable UUID userId, @RequestParam UUID workerId) {
        userService.removeFavoriteWorker(userId, workerId);
        return ResponseEntity.ok().build();
    }

    // Get Favorite Workers
    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<Favorite>> getFavoriteWorkers(@PathVariable UUID userId) {
        List<Favorite> favorites = userService.getFavoriteWorkers(userId);
        return ResponseEntity.ok(favorites);
    }
}