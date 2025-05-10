package com.example.searchService.strategy;

import com.example.searchService.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchStrategyFactory {

    private final CategorySearchStrategy categorySearchStrategy;
    private final LocationSearchStrategy locationSearchStrategy;
    private final AvailabilitySearchStrategy availabilitySearchStrategy;

    @Autowired
    public SearchStrategyFactory(
            CategorySearchStrategy categorySearchStrategy,
            LocationSearchStrategy locationSearchStrategy,
            AvailabilitySearchStrategy availabilitySearchStrategy) {
        this.categorySearchStrategy = categorySearchStrategy;
        this.locationSearchStrategy = locationSearchStrategy;
        this.availabilitySearchStrategy = availabilitySearchStrategy;
    }

    public SearchStrategy getStrategy(SearchRequest request) {
        boolean hasCategory = request.getCategory() != null && !request.getCategory().isEmpty();
        boolean hasLocation = request.getLocation() != null && !request.getLocation().isEmpty();
        boolean hasAvailability = request.isAvailable() != null; // Boolean wrapper check

        int count = 0;
        if (hasCategory) count++;
        if (hasLocation) count++;
        if (hasAvailability) count++;

        if (count != 1) {
            throw new IllegalArgumentException("You must provide exactly one field (either category, location, or availability).");
        }

        if (hasCategory) {
            return categorySearchStrategy;
        } else if (hasLocation) {
            return locationSearchStrategy;
        } else {
            return availabilitySearchStrategy;
        }
    }
}
