package com.example.reviewService.Observer;

import com.example.reviewService.Observer.ReviewObserver;

public interface ReviewSubject {
    void registerObserver(ReviewObserver observer);
    void removeObserver(ReviewObserver observer);
    void notifyObservers();
}
