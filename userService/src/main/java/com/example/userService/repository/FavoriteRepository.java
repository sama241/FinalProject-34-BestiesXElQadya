package com.example.userService.repository;

import com.example.userService.model.Favorite;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM favorites WHERE user_id = :userId AND worker_id = :workerId", nativeQuery = true)
    boolean existsByUserIdAndWorkerId(UUID userId, String workerId);

    List<Favorite> findByUserId(UUID userId);

    @Modifying
    @Query(value="DELETE FROM favorites WHERE user_id=:userId and worker_id=:workerId", nativeQuery = true)
    void deleteByUserIdAndWorkerId(UUID userId, String workerId);

    // delete all favorite records bta3et user mo3ayan
}
