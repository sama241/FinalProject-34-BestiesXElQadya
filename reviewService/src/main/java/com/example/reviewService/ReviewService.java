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


    // Create a new review using the Builder pattern
    public Review createReview(String workerId, String userId, int rating, String comment, boolean isAnonymous) {
        // Using the Builder to create a Review instance
        Review review = new Review.Builder()
                .workerId(workerId)
                .userId(userId)
                .rating(rating)
                .comment(comment)
                .isAnonymous(isAnonymous)
                .build();

        // Save the review to MongoDB and return the saved review with the auto-generated ID
        return reviewRepository.save(review);  // MongoDB will populate the `id` field automatically
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

    public double calculateAverageRating(String workerId) {
        List<Review> reviews = reviewRepository.findByWorkerId(workerId);  // Fetch reviews by worker ID
        if (reviews.isEmpty()) {
            return 0;  // If there are no reviews for the worker, return 0
        }
        // Calculate the average rating
        double totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return totalRating / reviews.size();  // Return the average rating
    }
    // Method to increment helpful votes
    public Review markReviewAsHelpful(String reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.incrementHelpfulVotes();  // Increment the helpful votes
            return reviewRepository.save(review);  // Save the updated review
        } else {
            throw new RuntimeException("Review not found");
        }
    }
}