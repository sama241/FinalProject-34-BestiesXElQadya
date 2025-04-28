package com.example.searchService.strategy;

import com.example.searchService.model.SearchRequest;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class CategorySearchStrategy implements SearchStrategy {

    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CategorySearchStrategy(MongoTemplate mongoTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Map<String, Object>> search(SearchRequest request) {
        String key = "search:category:" + request.getCategory();
        Object cachedResult = redisTemplate.opsForValue().get(key);

        if (cachedResult != null) {
            return (List<Map<String, Object>>) cachedResult;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("profession").is(request.getCategory()));

        List<Document> workers = mongoTemplate.find(query, Document.class, "workers");

        List<Map<String, Object>> workerDetailsList = new ArrayList<>();
        for (Document workerDoc : workers) {
            workerDetailsList.add(extractWorkerDetails(workerDoc));
        }

        redisTemplate.opsForValue().set(key, workerDetailsList, 10, TimeUnit.MINUTES);

        return workerDetailsList;
    }

    private Map<String, Object> extractWorkerDetails(Document doc) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", doc.getString("name"));
        map.put("profession", doc.getString("profession"));
        map.put("location", doc.getString("location"));
        map.put("skills", doc.getList("skills", String.class));
        map.put("availableHours", doc.getList("availableHours", Integer.class));
        map.put("badges", doc.getList("badges", String.class));
        map.put("isAvailable", doc.getBoolean("isAvailable"));
        return map;
    }
}
