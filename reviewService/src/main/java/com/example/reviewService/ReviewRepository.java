package com.example.reviewService;

import com.example.reviewService.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


 public interface ReviewRepository extends MongoRepository<Review, String> {

        // Fetch all reviews for a specific worker
        List<Review> findByWorkerId(String workerId);

        // Fetch all reviews created by a specific user (optional)
        List<Review> findByUserId(String userId);

        // 🗑️ Delete all reviews for a specific user (needed for user deletion)
        void deleteByUserId(String userId);

    }
