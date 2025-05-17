package com.example.workerService.cache;
import com.example.workerService.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class WorkerCacheService {

    private final RedisTemplate<String, Worker> redisTemplate;
    private final ValueOperations<String, Worker> valueOps;

    @Autowired
    public WorkerCacheService(RedisTemplate<String, Worker> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    public void cacheWorker(Worker worker, long ttlMinutes) {
        valueOps.set(worker.getId(), worker, Duration.ofMinutes(ttlMinutes));
    }

    public Worker getCachedWorker(String id) {
        return valueOps.get(id);
    }

    public void deleteWorker(String id) {
        redisTemplate.delete(id);
    }

    public boolean isCached(String id) {
        return redisTemplate.hasKey(id);
    }
}