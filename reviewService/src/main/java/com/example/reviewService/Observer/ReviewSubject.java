package com.example.reviewService.Observer;

import java.util.Observer;

public interface ReviewSubject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
