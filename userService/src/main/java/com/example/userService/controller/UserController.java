package com.example.userService.controller;

import com.example.userService.model.Favorite;
import com.example.userService.model.User;
import com.example.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.userService.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


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
    @PutMapping("/{userId}/username")
    public ResponseEntity<User> updateUsername(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.updateUsername(userId, body.get("username")));
    }

    @PutMapping("/{userId}/name")
    public ResponseEntity<User> updateName(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.updateName(userId, body.get("name")));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<User> updatePassword(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.updatePassword(userId, body.get("password")));
    }

    @PutMapping("/{userId}/email")
    public ResponseEntity<User> updateEmail(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.updateEmail(userId, body.get("email")));
    }

    @PutMapping("/{userId}/phone")
    public ResponseEntity<User> updatePhone(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.updatePhone(userId, body.get("phone")));
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<User> updateAddress(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.updateAddress(userId, body.get("address")));
    }

//    @PutMapping("/{userId}")
//    public User updateUser(@PathVariable UUID userId, @RequestBody User userDetails) {
//        //User updatedUser = userService.updateUser(userId, user);
//        Optional<User> optional = userRepository.findById(userId);
//        if (optional.isPresent()) {
//            User user = optional.get();
//
//            if (userDetails.getUsername() != null) {
//                user.setUsername(userDetails.getUsername());
//            }
//
//            if (userDetails.getName() != null) {
//                user.setName(userDetails.getName());
//            }
//
//            if (userDetails.getPassword() != null) {
//                user.setPassword(userDetails.getPassword());
//            }
//
//            if (userDetails.getEmail() != null) {
//                user.setEmail(userDetails.getEmail());
//            }
//
//            if (userDetails.getPhone() != null) {
//                user.setPhone(userDetails.getPhone());
//            }
//
//            if (userDetails.getAddress() != null) {
//                user.setAddress(userDetails.getAddress());
//            }
//
//             userRepository.save(user);
//            return user;
//        }
//        return null;
//    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User updatedUser) {
        Optional<User> existingUserOptional = Optional.ofNullable(userService.getUserById(id));  // assumes this method exists

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            if (updatedUser.getUsername() != null) {
                existingUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getName() != null) {
                existingUser.setName(updatedUser.getName());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPhone() != null) {
                existingUser.setPhone(updatedUser.getPhone());
            }
            if (updatedUser.getAddress() != null) {
                existingUser.setAddress(updatedUser.getAddress());
            }

            userService.updateUser(existingUser);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


//    @PutMapping("/{userId}")
//    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
//        return userService.updateUser(id, user);
//    }


    // Delete User by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }


    // Favorite Worker Functions

    @Post