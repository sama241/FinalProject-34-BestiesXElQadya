package com.example.userService.service;


import com.example.userService.model.Favorite;
import com.example.userService.model.User;
import com.example.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.userService.repository.FavoriteRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;



    // CRUD Operations for User

    //get all
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // get by username
    public Optional<User> findByUsername(String username) {
            return userRepository.findByUsername(username);
    }

    // get by email


    // Create a new User
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get a User by ID
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // get by username
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // get by email
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // get by phone
    public Optional<User> getByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    // Update User details
    // say if we update haga haga mesh kolo?
    public User updateUser(UUID userId, User userDetails) {
        User user = getUserById(userId);
        user.setUsername(userDetails.getUsername());
        user.setName(userDetails.getName());
        user.setPassword(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setAddress(userDetails.getAddress());
        return userRepository.save(user);
    }

    // Delete User by ID
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    // Favorite Worker Functions
    // Add a worker to the user's favorites list
    public void addFavoriteWorker(UUID userId, UUID workerId) {
//        if (favoriteRepository.existsByUserIdAndWorkerId(userId, workerId)) {
//            throw new RuntimeException("Worker is already in your favorites list.");
//        }

        Favorite favorite = new Favorite(userId, workerId);
        favoriteRepository.save(favorite);
    }

    // Remove a worker from the user's favorites list
    public void removeFavoriteWorker(UUID userId, UUID workerId) {
        if (!favoriteRepository.existsByUserIdAndWorkerId(userId, workerId)) {
            throw new RuntimeException("Favorite worker not found.");
        }

        favoriteRepository.deleteByUserIdAndWorkerId(userId, workerId);
    }

    // Get the list of favorite workers for a user
    public List<Favorite> getFavoriteWorkers(UUID userId) {
        return favoriteRepository.findByUserId(userId);
    }
}
