package com.example.reviewService.Observer;

import com.example.reviewService.rabbitmq.ReviewProducer;

public class WorkerObserver implements ReviewObserver {

    private final ReviewProducer reviewProducer;
    private final String workerId;

    public WorkerObserver(ReviewProducer reviewProducer, String workerId) {
        this.reviewProducer = reviewProducer;
        this.workerId = workerId;
    }

    @Override
    public void update(int averageRating) {
        reviewProducer.sendReviewToWorker(workerId, averageRating);
    }
}
