package com.example.userService.service;

import com.example.userService.model.Favorite;
import com.example.userService.model.User;
import com.example.userService.repository.UserRepository;
import com.example.userService.repository.FavoriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;



    // CRUD Operations for User

    //get all
    public List<User> getUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    // get by username
    public Optional<User> findByUsername(String username) {
        logger.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    // get by email


    // Create a new User
    public User createUser(User user) {
        logger.info("Creating new user with username: {}", user.getUsername());
        return userRepository.save(user);
    }

    // Get a User by ID
    public User getUserById(UUID userId) {
        logger.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> {
            logger.error("User not found with ID: {}", userId);
            return new RuntimeException("User not found");
        });
    }

    // get by username
    public Optional<User> getByUsername(String username) {
        logger.info("Getting user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    // get by email
    public User getByEmail(String email) {
        logger.info("Getting user by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }


    // get by phone
    public Optional<User> getByPhone(String phone) {
        logger.info("Getting user by phone: {}", phone);
        return userRepository.findByPhone(phone);
    }
    public boolean existsByUserId(UUID userId) {
        return userRepository.existsById(userId);
    }

    // Update User details
    // say if we update haga haga mesh kolo?
    public User updateUser(UUID userId, User userDetails) {
        logger.info("Updating user with ID: {}", userId);
        User user = getUserById(userId);

        // Check and update each field only if the new value is not null
        if (userDetails.getUsername() != null) {
            user.setUsername(userDetails.getUsername());
        }

        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }

        if (userDetails.getPassword() != null) {
            user.setPassword(userDetails.getPassword());
        }

        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }

        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }

        // Save the updated user to the repository and return
        return userRepository.save(user);
    }


    // Delete User by ID
    public void deleteUser(UUID userId) {
        logger.info("Deleting user with ID: {}", userId);
        userRepository.deleteById(userId);
    }

    // Favorite Worker Functions
    // Add a worker to the user's favorites list
//    public void addFavoriteWorker(UUID userId, UUID workerId) {

//        System.out.print(userId);
//        System.out.print(workerId);
//
//        Favorite favorite = new Favorite(userId, workerId);
//        System.out.println(favorite);
//        favoriteRepository.save(favorite);
//    }

    @Transactional
    public Favorite addFavoriteWorker(Favorite favorite) {
        logger.info("Adding favorite worker {} for user {}", favorite.getWorkerId(), favorite.getUserId());

        if (favoriteRepository.existsByUserIdAndWorkerId(favorite.getUserId(), favorite.getWorkerId())) {
            logger.warn("Worker {} is already in the favorites list for user {}", favorite.getWorkerId(), favorite.getUserId());
            return null;
        }

        return favoriteRepository.save(favorite);
    }


    // Remove a worker from the user's favorites list
    @Transactional
    public String removeFavoriteWorker(UUID userId, String workerId) {
        logger.info("Removing favorite worker {} for user {}", workerId, userId);

        if (!favoriteRepository.existsByUserIdAndWorkerId(userId, workerId)) {
            logger.warn("Favorite worker not found for user {} and worker {}", userId, workerId);
            return "Favorite worker not found.";
        }

        favoriteRepository.deleteByUserIdAndWorkerId(userId, workerId);
        return "Worker removed from favorites.";
    }

    // Get the list of favorite workers for a user
    public List<Favorite> getFavoriteWorkers(UUID userId) {
        logger.info("Getting favorite workers for user {}", userId);
        return favoriteRepository.findByUserId(userId);
    }
}
