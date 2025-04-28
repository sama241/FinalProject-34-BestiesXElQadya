package service;


import model.Favorite;
import model.User;
import repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.FavoriteRepository;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    // CRUD Operations for User
    // Create a new User
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get a User by ID
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Update User details
    // Update User details
    public User updateUser(UUID userId, User userDetails) {
        User user = getUserById(userId);
        user.setUsername(userDetails.getUsername());
        user.setName(userDetails.getName());
        user.setPassword(userDetails.getPassword());  // Ensure password is hashed
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
        if (favoriteRepository.existsByUserIdAndWorkerId(userId, workerId)) {
            throw new RuntimeException("Worker is already in your favorites list.");
        }

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
