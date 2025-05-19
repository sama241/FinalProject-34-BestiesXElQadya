package com.example.reviewService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @Indexed
    private String workerId;

    @Indexed
    private String userId;

    private int rating;
    private String comment;
    private Boolean isAnonymous;
    private int helpfulVotes = 0;


    public int getHelpfulVotes() {
        return helpfulVotes;
    }

    public void setHelpfulVotes(int helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public void incrementHelpfulVotes() {
        this.helpfulVotes++;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public Review() {}

    // Private constructor for the Builder pattern
    private Review(Builder builder) {
        this.id = builder.id != null ? builder.id : null;  // Leave id null for MongoDB to generate it
        this.workerId = builder.workerId;
        this.userId = builder.userId;
        this.rating = builder.rating;
        this.comment = builder.comment;
        this.isAnonymous = builder.isAnonymous;
    }

    public String getId() {
        return id;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getUserId() {
        return userId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        isAnonymous = anonymous;
    }

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

    public static class Builder {
        private String id;
        private String workerId;
        private String userId;
        private int rating;
        private String comment = "";
        private boolean isAnonymous = false;

        public Builder workerId(String workerId) {
            this.workerId = workerId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder isAnonymous(boolean isAnonymous) {
            this.isAnonymous = isAnonymous;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }
}
