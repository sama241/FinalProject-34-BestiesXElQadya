package com.example.reviewService.Observer;

import com.example.reviewService.Observer.ReviewObserver;
import com.example.reviewService.event.ReviewEvent;

public interface ReviewSubject {
    void registerObserver(ReviewObserver observer);
    void removeObserver(ReviewObserver observer);
    void notifyObservers(ReviewEvent event);
}
