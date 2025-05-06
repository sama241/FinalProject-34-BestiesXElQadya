package com.example.reviewService.Observer;

import com.example.reviewService.event.ReviewEvent;

import java.util.ArrayList;
import java.util.List;

public class ReviewManager implements ReviewSubject {

    private final List<ReviewObserver> observers;

    public ReviewManager() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(ReviewObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ReviewObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ReviewEvent event) {
        for (ReviewObserver observer : observers) {
            observer.update(event);
        }
    }

    // You can have a method to add a review and notify observers
    public void addReview(ReviewEvent reviewEvent) {
        // Logic for adding the review
        // Once review is added, notify observers
        notifyObservers(reviewEvent);
    }
}
