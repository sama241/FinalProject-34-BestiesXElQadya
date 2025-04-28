package com.example.reviewService;

import com.example.reviewService.model.Review;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Create a new review
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    // Read a review by ID
    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    // Get all reviews for a worker
    public List<Review> getReviewsByWorkerId(String workerId) {
        return reviewRepository.findByWorkerId(workerId);
    }

    // Get all reviews by a user
    public List<Review> getReviewsByUserId(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    // Update a review (re-save it)
    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    // Delete a review by ID
    public void deleteReviewById(String id) {
        reviewRepository.deleteById(id);
    }
}