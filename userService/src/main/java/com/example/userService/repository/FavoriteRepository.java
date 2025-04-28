package com.example.userService.repository;

import com.example.userService.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    boolean existsByUserIdAndWorkerId(UUID userId, UUID workerId);

    List<Favorite> findByUserId(UUID userId);

    void deleteByUserIdAndWorkerId(UUID userId, UUID workerId);

    // delete all favorite records bta3et user mo3ayan
}
