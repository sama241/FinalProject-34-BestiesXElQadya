package com.example.searchService.strategy;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class AvailabilitySearchStrategy implements SearchStrategy {

    private final WorkerClient workerClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AvailabilitySearchStrategy(WorkerClient workerClient, RedisTemplate<String, Object> redisTemplate, MongoTemplate mongoTemplate) {
        this.workerClient = workerClient;
        this.redisTemplate = redisTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Map<String, Object>> search(SearchRequest request) {
        String key = "search:availability:" + request.isAvailable();
        Object cachedResult = redisTemplate.opsForValue().get(key);

        if (cachedResult != null) {
            return (List<Map<String, Object>>) cachedResult;
        }

        List<Map<String, Object>> workers = workerClient.getWorkers();
        List<Map<String, Object>> filteredWorkers = new ArrayList<>();

        if (workers != null) {
            for (Map<String, Object> worker : workers) {
                if (worker.get("available") != null && worker.get("available").equals(request.isAvailable())) {
                    worker.remove("password");
                    filteredWorkers.add(worker);
                }
            }
        }

        redisTemplate.opsForValue().set(key, filteredWorkers, 10, TimeUnit.MINUTES);
        return filteredWorkers;
    }
}
