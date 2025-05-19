package com.example.userService.repository;

import com.example.userService.model.Favorite;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, String> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM favorites WHERE user_id = :userId AND worker_id = :workerId", nativeQuery = true)
    boolean existsByUserIdAndWorkerId(String userId, String workerId);

    List<Favorite> findByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM favorites WHERE user_id = :userId AND worker_id = :workerId", nativeQuery = true)
    void deleteByUserIdAndWorkerId(String userId, String workerId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM favorites WHERE user_id = :userId", nativeQuery = true)
    void deleteAllByUserId(String userId);
}
