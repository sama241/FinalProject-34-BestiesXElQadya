package com.example.reviewService;

import com.example.reviewService.model.Review;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Create a new review
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review createdReview = reviewService.createReview(review);
        return ResponseEntity.ok(createdReview);
    }

    // Get a review by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all reviews for a specific worker
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<Review>> getReviewsByWorkerId(@PathVariable String workerId) {
        List<Review> reviews = reviewService.getReviewsByWorkerId(workerId);
        return ResponseEntity.ok(reviews);
    }

    // Get all reviews made by a specific user (optional)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable String userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    // Update a review (optional: same endpoint as create but different HTTP method)
    @PutMapping("/{id}")
//    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody Review updatedReview) {
//        // First check if the review exists
//        Optional<Review> existingReview = reviewService.getReviewById(id);
//
//        if (existingReview.isPresent()) {
//            Review reviewToUpdate = new Review.Builder(
//                    id,
//                    updatedReview.getWorkerId(),
//                    updatedReview.getUserId(),
//                    updatedReview.getRating()
//            )
//                    .comment(updatedReview.getComment())
//                    .tags(updatedReview.getTags())
//                    .anonymous(updatedReview.isAnonymous())
//                    .helpfulVotes(updatedReview.getHelpfulVotes())
//                    .timestamp(updatedReview.getTimestamp())
//                    .build();
//
//            Review savedReview = reviewService.updateReview(reviewToUpdate);
//            return ResponseEntity.ok(savedReview);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Delete a review by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReviewById(id);
        return ResponseEntity.noContent().build();
    }
}
