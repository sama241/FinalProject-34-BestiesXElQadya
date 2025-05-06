package com.example.userService.controller;

import com.example.userService.model.Favorite;
import com.example.userService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.userService.service.UserService;

import java.util.List;
import java.util.Optional;
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

    // Get User by Username
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> optionalUser = userService.getByUsername(username);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get User by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User optionalUser = userService.getByEmail(email);

        if (optionalUser!=null) {
            return ResponseEntity.ok(optionalUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get User by Phone
    @GetMapping("/phone/{phone}")
    public ResponseEntity<User> getUserByPhone(@PathVariable String phone) {
        Optional<User> optionalUser = userService.getByPhone(phone);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
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