package com.example.reviewService.Observer;

import com.example.reviewService.event.ReviewEvent;
import com.example.reviewService.model.Review;

public interface ReviewObserver {
    void update(ReviewEvent event);

}
