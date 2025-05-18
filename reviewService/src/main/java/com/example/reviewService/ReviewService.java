package com.example.reviewService;

import com.example.reviewService.model.Review;
import com.example.reviewService.rabbitmq.ReviewProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final ReviewProducer reviewProducer;

    public ReviewService(ReviewRepository reviewRepository, ReviewProducer reviewProducer) {
        this.reviewRepository = reviewRepository;
        this.reviewProducer = reviewProducer;
    }

    public Review createReview(String workerId, String userId, int rating, String comment, boolean isAnonymous) {
        try {
            Review review = new Review.Builder()
                    .workerId(workerId)
                    .userId(userId)
                    .rating(rating)
                    .comment(comment)
                    .isAnonymous(isAnonymous)
                    .build();

            Review savedReview = reviewRepository.save(review);
            logger.info("Review created by user {} for worker {}", userId, workerId);

            double newAverage = calculateAverageRating(workerId);
            reviewProducer.sendReviewToWorker(workerId, newAverage);
            logger.info("Sent new average rating ({}) to worker {}", newAverage, workerId);

            return savedReview;
        } catch (Exception e) {
            logger.error("Failed to create review for worker {} by user {}", workerId, userId, e);
            throw e;
        }
    }

    public Optional<Review> getReviewById(String id) {
        logger.info("Fetching review with ID {}", id);
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewsByWorkerId(String workerId) {
        logger.info("Fetching all reviews for worker {}", workerId);
        return reviewRepository.findByWorkerId(workerId);
    }

    public List<Review> getReviewsByUserId(String userId) {
        logger.info("Fetching all reviews by user {}", userId);
        return reviewRepository.findByUserId(userId);
    }

    public Review updateReview(Review review) {
        logger.info("Updating review with ID {}", review.getId());
        return reviewRepository.save(review);
    }

    public void deleteReviewById(String id) {
        logger.info("Deleting review with ID {}", id);
        reviewRepository.deleteById(id);
    }

    public double calculateAverageRating(String workerId) {
        List<Review> reviews = reviewRepository.findByWorkerId(workerId);
        if (reviews.isEmpty()) {
            logger.info("No reviews found for worker {}, returning average as 0", workerId);
            return 0;
        }

        double totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }

        double avg = totalRating / reviews.size();
        logger.info("Calculated average rating ({}) for worker {}", avg, workerId);
        return avg;
    }

    public Review markReviewAsHelpful(String reviewId) {
        logger.info("Marking review {} as helpful", reviewId);

        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.incrementHelpfulVotes();
            logger.info("Helpful votes incremented for review {}", reviewId);
            return reviewRepository.save(review);
        } else {
            logger.error("Review with ID {} not found", reviewId);
            throw new RuntimeException("Review not found");
        }
    }
}
