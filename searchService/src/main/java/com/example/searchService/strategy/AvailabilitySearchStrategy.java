package com.example.searchService.strategy;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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
            // Return cached result if available
            return (List<Map<String, Object>>) cachedResult;
        }

        // If no cached result, query the database
        Query query = new Query();
        query.addCriteria(Criteria.where("isAvailable").is(request.isAvailable()));
        List<Map<String, Object>> workers = workerClient.getWorkers();

        // Filter workers based on availability
        List<Map<String, Object>> filteredWorkers = new ArrayList<>();
        for (Map<String, Object> worker : workers) {
            if (worker.get("isAvailable") != null && worker.get("isAvailable").equals(request.isAvailable())) {
                filteredWorkers.add(worker);
            }
        }

        // Cache the result for future use
        redisTemplate.opsForValue().set(key, filteredWorkers, 10, TimeUnit.MINUTES);

        return filteredWorkers;
    }
}
