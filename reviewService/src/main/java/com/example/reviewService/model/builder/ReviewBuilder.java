package com.example.reviewService.model.builder;

import com.example.reviewService.model.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReviewBuilder {

    private String workerId;
    private String userId;
    private int rating;
    private String comment = "";
    private boolean isAnonymous = false;
    private int helpfulVotes = 0;
    private List<String> voterIds = new ArrayList<>();


    public ReviewBuilder workerId(String workerId) {
        this.workerId = workerId;
        return this;
    }

    public ReviewBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public ReviewBuilder rating(int rating) {
        this.rating = rating;
        return this;
    }

    public ReviewBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public ReviewBuilder isAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
        return this;
    }

    public ReviewBuilder helpfulVotes(int helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
        return this;
    }

    public ReviewBuilder voterIds(List<String> voterIds) {
        this.voterIds = voterIds;
        return this;
    }

    public Review build() {
        //validation
        Objects.requireNonNull(workerId, "workerId cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");

        Review review = new Review(workerId, userId, rating);
        review.setComment(comment);
        review.setAnonymous(isAnonymous);
        review.setHelpfulVotes(helpfulVotes);
        review.setVoterIds(voterIds);
        return review;
    }
}
