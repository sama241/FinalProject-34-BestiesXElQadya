package com.example.searchService.strategy;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class CategorySearchStrategy implements SearchStrategy {

    private final WorkerClient workerClient;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CategorySearchStrategy(WorkerClient workerClient, RedisTemplate<String, Object> redisTemplate) {
        this.workerClient = workerClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Map<String, Object>> search(SearchRequest request) {
        // Cache key based on the category
        String key = "search:category:" + request.getCategory();
        Object cachedResult = redisTemplate.opsForValue().get(key);

        if (cachedResult != null) {
            // Return cached result if available
            return (List<Map<String, Object>>) cachedResult;
        }

        // Query the workers if the cache is empty
        List<Map<String, Object>> workers = workerClient.getWorkers();

        // Filter workers by category
        List<Map<String, Object>> filteredWorkers = new ArrayList<>();
        for (Map<String, Object> worker : workers) {
            if (worker.get("profession") != null && worker.get("profession").equals(request.getCategory())) {
                worker.remove("password");
                filteredWorkers.add(worker);
            }
        }

        // Cache the result for future use
        redisTemplate.opsForValue().set(key, filteredWorkers, 10, TimeUnit.MINUTES);

        return filteredWorkers;
    }
}
