package com.example.reviewService;

import com.example.reviewService.model.Review;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    @Test
    void testReviewBuilder() {
        // Create a Review using the Builder
        Review review = new Review.Builder()
                .workerId("worker123")
                .userId("user456")
                .rating(5)
                .comment("Great work!")
                .isAnonymous(false)
                .build();

        // Assert that the builder created a valid Review object with the expected values
        assertNotNull(review);
        assertEquals("worker123", review.getWorkerId());
        assertEquals("user456", review.getUserId());
        assertEquals(5, review.getRating());
        assertEquals("Great work!", review.getComment());
        assertFalse(review.getIsAnonymous());
    }

    @Test
    void testReviewBuilderWithDefaults() {
        // Test default values for some fields
        Review review = new Review.Builder()
                .workerId("worker123")
                .userId("user456")
                .rating(4)
                .build();

        // Assert that defaults are correctly applied
        assertNotNull(review);
        assertEquals("worker123", review.getWorkerId());
        assertEquals("user456", review.getUserId());
        assertEquals(4, review.getRating());
        assertEquals("", review.getComment()); // Default empty string for comment
        assertFalse(review.getIsAnonymous());  // Default value is false

    }
}
