package com.example.reviewService;

import com.example.reviewService.Client.workerClient;
import com.example.reviewService.Observer.ReviewData;
import com.example.reviewService.Observer.WorkerObserver;
import com.example.reviewService.model.Rating;
import com.example.reviewService.model.Review;
import com.example.reviewService.rabbitmq.ReviewProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {


    @Autowired
    private ReviewProducer reviewProducer;

    private final ReviewRepository reviewRepository;

    private final workerClient WorkerClient ;

    public ReviewService(ReviewRepository reviewRepository, workerClient WorkerClient) {
        this.reviewRepository = reviewRepository;
        this.WorkerClient = WorkerClient;
    }


    // Create a new review using the Builder pattern
    public Review createReview(String workerId, String userId, int rating, String comment, boolean isAnonymous) {
        try {
            ResponseEntity<?> response = WorkerClient.getWorker(workerId);
            if (response.getStatusCode().is4xxClientError()) {
                throw new RuntimeException("Worker with ID " + workerId + " does not exist.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error validating worker existence: " + e.getMessage());
        }

        Rating validRating = Rating.fromValue(rating);
        Review review = new Review.Builder()
                .workerId(workerId)
                .userId(userId)
                .rating(validRating.getValue())
                .comment(comment)
                .isAnonymous(isAnonymous)
                .build();

        Review savedReview = reviewRepository.save(review);

        double newAverage = calculateAverageRating(workerId);
        System.out.println("Calling WorkerService to update average for: " + workerId);
        // Use Feign to notify WorkerService to update the worker's average rating

        System.out.println("Calling WorkerService to update average: " + newAverage);
        WorkerClient.updateAverageRating(workerId, newAverage);
        ReviewData reviewData = new ReviewData();
        reviewData.registerObserver(new WorkerObserver(reviewProducer, workerId));
        reviewData.setAverageRating((int) newAverage);
        System.out.println("Update call done");
        return savedReview;
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

    // Fetch reviews by workerId and filter out anonymous reviews by userId
    public List<Review> getReviewsByWorkerIdAndUserId(String workerId, String userId) {
        return reviewRepository.findByWorkerIdAndUserIdAndIsAnonymous(workerId, userId, false);
    }
    public List<Review> getallReviewsByWorkerId(String workerId) {
        // Fetch all reviews for the worker
        List<Review> reviews = reviewRepository.findByWorkerId(workerId);

        // Return the reviews to be processed by the controller
        return reviews;
    }


}