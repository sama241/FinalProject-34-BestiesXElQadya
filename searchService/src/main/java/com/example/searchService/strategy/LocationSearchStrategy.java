package com.example.searchService.strategy;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class LocationSearchStrategy implements SearchStrategy {

    private final WorkerClient workerClient;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public LocationSearchStrategy(WorkerClient workerClient, RedisTemplate<String, Object> redisTemplate) {
        this.workerClient = workerClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Map<String, Object>> search(SearchRequest request) {
        // Cache key based on the location
        String key = "search:location:" + request.getLocation();
        Object cachedResult = redisTemplate.opsForValue().get(key);

        if (cachedResult != null) {
            // Return cached result if available
            return (List<Map<String, Object>>) cachedResult;
        }


        // Query the workers if the cache is empty
        List<Map<String, Object>> workers = workerClient.getWorkers();

        // Filter workers by location
        List<Map<String, Object>> filteredWorkers = new ArrayList<>();
        for (Map<String, Object> worker : workers) {
            if (worker.get("location") != null && worker.get("location").equals(request.getLocation())) {
                worker.remove("password");
                filteredWorkers.add(worker);
            }
        }

        // Cache the result for future use
        redisTemplate.opsForValue().set(key, filteredWorkers, 2, TimeUnit.MINUTES);

        return filteredWorkers;
    }
}