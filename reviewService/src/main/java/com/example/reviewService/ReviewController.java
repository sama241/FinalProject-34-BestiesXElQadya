package com.example.reviewService;

import com.example.reviewService.model.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Create a new review

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public ResponseEntity<Review> createReview(@RequestHeader("X-User-Id") String userId ,@RequestBody Review review) {

        // Create and save the review using the service
        Review savedReview = reviewService.createReview(
                review.getWorkerId(),
                userId,
                review.getRating(),
                review.getComment(),
                review.getIsAnonymous()
        );

        // Return the saved review, now including the generated `id`
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    // Get a review by its ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all reviews for a specific worker
    @GetMapping("/worker")  //hatghyr
    public ResponseEntity<List<Review>> getReviewsByWorkerId(@RequestHeader("X-Worker-Id") String workerId) {
        List<Review> reviews = reviewService.getReviewsByWorkerId(workerId);
        return ResponseEntity.ok(reviews);
    }


    // Get all reviews made by a specific user (optional)
    @GetMapping("/user") // di hattghyr


    public ResponseEntity<List<Review>> getReviewsByUserId(@RequestHeader("X-User-Id") String userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    // Update a review (optional: same endpoint as create but different HTTP method)
    @PutMapping("/user/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody Review updatedReview) {
        Optional<Review> existingReviewOptional = reviewService.getReviewById(id);

        if (existingReviewOptional.isPresent()) {
            // Get the existing review from the database
            Review existingReview = existingReviewOptional.get();

            // Update the fields of the existing review
            if (updatedReview.getWorkerId() != null) {
                existingReview.setWorkerId(updatedReview.getWorkerId());
            }
            if (updatedReview.getUserId() != null) {
                existingReview.setUserId(updatedReview.getUserId());
            }
            if (updatedReview.getRating() != 0) {
                existingReview.setRating(updatedReview.getRating());
            }
            if (updatedReview.getComment() != null) {
                existingReview.setComment(updatedReview.getComment());
            }
            if (updatedReview.getIsAnonymous() != null) {
                existingReview.setAnonymous(updatedReview.getIsAnonymous());
            }

            // Save the updated review
            reviewService.updateReview(existingReview);

            // Return the updated review as the response
            return ResponseEntity.ok(existingReview);  // Returning the updated review
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a review by ID
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReviewById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to mark a review as helpful
    @PutMapping("/user/{reviewId}/helpful")
    public ResponseEntity<Review> markReviewAsHelpful(@PathVariable String reviewId) {
        try {
            Review updatedReview = reviewService.markReviewAsHelpful(reviewId);  // Increment helpful votes
            return ResponseEntity.ok(updatedReview);  // Return the updated review
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Review not found
        }
    }

    // Get the average rating for a worker
    @GetMapping("/user/average-rating")
    public ResponseEntity<Double> getAverageRatingForUser(@RequestHeader("X-User-Id") String userId,@RequestParam String workerId) {
        double averageRating = reviewService.calculateAverageRating(workerId);  // Calculate the average rating
        return ResponseEntity.ok(averageRating);  // Return the average rating in the response
    }

    @GetMapping("/worker/average-rating")
    public ResponseEntity<Double> getAverageRatingForWorker(@RequestHeader("X-Worker-Id") String workerId) {
        double averageRating = reviewService.calculateAverageRating(workerId);  // Calculate the average rating
        return ResponseEntity.ok(averageRating);  // Return the average rating in the response
    }

    // ana worker w badwr bl user id maynf3sh yetl3ly el reviews el annoynoums el hwa 3amelha
    @GetMapping("/worker/findbyworker/findbyuser") //hattghyr
    public ResponseEntity<List<Review>> getReviewsByWorkerAndUser(
            @RequestHeader("X-Worker-Id")  String workerId,
            @RequestParam String userId) {

        System.out.println("WorkerId aanna : " + workerId + ", UserId: " + userId);
        List<Review> reviews = reviewService.getReviewsByWorkerIdAndUserId(workerId, userId);

        if (reviews.isEmpty()) {
            // If no reviews are found, return a 404 (Not Found)
            return ResponseEntity.notFound().build();
        } else {
            // Return the list of reviews along with a 200 OK response
            return ResponseEntity.ok(reviews);
        }
    }
//ana worker badwr ala kol el reviews el ma3mola 3alaya law review fehom annonymous maynf3sh ashof el userid bta3o
    @GetMapping("/worker/getallreviews") //hattghyr
    public ResponseEntity<List<Review>> getReviewsByWorkerIdAndHideAnonymousUser(
            @RequestHeader("X-Worker-Id") String workerId) {

        System.out.println("WorkerId: " + workerId);
        List<Review> reviews = reviewService.getallReviewsByWorkerId(workerId);

        // Hide userId for anonymous reviews
        reviews.forEach(review -> {
            if (review.getIsAnonymous()) {
                review.setUserId(null); // Remove the userId for anonymous reviews
            }
        });

//        if (reviews.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
            return ResponseEntity.ok(reviews); // Return reviews with userId hidden for anonymous reviews

    }
}
