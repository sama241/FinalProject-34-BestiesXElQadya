package com.example.reviewService.Observer;

import com.example.reviewService.event.ReviewEvent;
import com.example.reviewService.rabbitmq.ReviewProducer;
public interface ReviewObserver {
    void update(int averageRating);
}
