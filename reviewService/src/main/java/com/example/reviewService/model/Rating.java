package com.example.reviewService.model;

import java.util.Arrays;

public enum Rating {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;

    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Optional: Validate input
    public static Rating fromValue(int value) {
        return Arrays.stream(values())
                .filter(r -> r.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid rating value"));
    }

}


