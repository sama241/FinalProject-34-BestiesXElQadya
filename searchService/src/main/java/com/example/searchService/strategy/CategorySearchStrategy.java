package com.example.searchService.strategy;

import com.example.searchService.model.SearchRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;

@Component
public class CategorySearchStrategy implements SearchStrategy {

    private final RedisTemplate<String, Object> redisTemplate;

    public CategorySearchStrategy(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<String> search(SearchRequest request) {
        String key = "search:category:" + request.getCategory();
        Object cachedResult = redisTemplate.opsForValue().get(key);

        if (cachedResult != null) {
            return (List<String>) cachedResult;
        }

        List<String> searchResults = new ArrayList<>();  // TODO: connect DB later
        redisTemplate.opsForValue().set(key, searchResults);
        return searchResults;
    }
}
