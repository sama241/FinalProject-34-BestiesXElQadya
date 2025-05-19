package com.example.userService.repository;


import com.example.userService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    // Find user by username
    Optional<User> findByUsername(String username);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by phone
    Optional<User> findByPhone(String phone);

    // Find user by ID (this is already covered by the JpaRepository's findById method)
    Optional<User> findById(String id);

    // Delete user by ID (this is also provided by JpaRepository's deleteById method)
    void deleteById(String id);
}
