package com.example.reviewService.event;

public class ReviewEvent {
    private final String workerId;
    private final int rating;

    public ReviewEvent(String workerId, int rating) {
        this.workerId = workerId;
        this.rating = rating;
    }

    public String getWorkerId() {
        return workerId;
    }

    public int getRating() {
        return rating;
    }
}
