package com.example.reviewService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "reviews")
public class Review {

    //attributes

    @Id
    private String id;

    @Indexed
    private String workerId;

    @Indexed
    private String userId;

    private int rating;

    private String comment;
    private boolean isAnonymous;

    private int helpfulVotes;
    private List<String> voterIds;


    public Review() {}

    public Review(String workerId, String userId, int rating) {
        this.workerId = Objects.requireNonNull(workerId);
        this.userId = Objects.requireNonNull(userId);
        this.setRating(rating); // Validation
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getWorkerId() { return workerId; }
    public void setWorkerId(String workerId) { this.workerId = workerId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public List<String> getVoterIds() {
        return voterIds;
    }
    public void setVoterIds(List<String> voterIds) {
        this.voterIds = voterIds;
    }

    public int getHelpfulVotes() {
        return helpfulVotes;
    }
    public void setHelpfulVotes(int helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }
    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    // equals(), hashCode(), toString()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", workerId='" + workerId + '\'' +
                ", rating=" + rating +
                ", isAnonymous=" + isAnonymous +
                '}';
    }
}