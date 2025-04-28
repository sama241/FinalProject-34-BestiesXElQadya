package com.example.searchService.strategy;

import com.example.searchService.model.SearchRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;

@Component
public class AvailabilitySearchStrategy implements SearchStrategy {

    private final RedisTemplate<String, Object> redisTemplate;

    public AvailabilitySearchStrategy(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<String> search(SearchRequest request) {
        String key = "search:availability:" + request.isAvailable();
        Object cachedResult = redisTemplate.opsForValue().get(key);

        if (cachedResult != null) {
            return (List<String>) cachedResult;
        }

        List<String> searchResults = new ArrayList<>();  // TODO: connect DB later
        redisTemplate.opsForValue().set(key, searchResults);
        return searchResults;
    }
}
