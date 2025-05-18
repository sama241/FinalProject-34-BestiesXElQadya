package com.example.reviewService.Observer;


import java.util.ArrayList;
import java.util.List;

public class ReviewData implements ReviewSubject {
    private List<ReviewObserver> observers;
    private int averageRating;

    public ReviewData() {
        observers = new ArrayList<ReviewObserver>();
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
    public void notifyObservers() {
        for (ReviewObserver observer : observers) {
            observer.update(averageRating);
        }

    }

    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
        averageRatingChanged();
    }

    public void averageRatingChanged() {
        notifyObservers();
    }
}
