package com.example.userService.singleton;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserSessionManager {

    private static UserSessionManager instance;

    private final StringRedisTemplate redisTemplate;
    private static final String ACTIVE_USERS_KEY = "activeUsers";

    // Private constructor
    private UserSessionManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Lazy singleton instance accessor
    public static synchronized UserSessionManager getInstance(StringRedisTemplate redisTemplate) {
        if (instance == null) {
            instance = new UserSessionManager(redisTemplate);
        }
        return instance;
    }

//    public void addActiveUser(String sessionId) {
//        redisTemplate.opsForSet().add(ACTIVE_USERS_KEY, sessionId);
//    }
//
//    public void removeActiveUser(String sessionId) {
//        redisTemplate.opsForSet().remove(ACTIVE_USERS_KEY, sessionId);
//    }
//
//    public boolean isUserActive(String sessionId) {
//        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ACTIVE_USERS_KEY, sessionId));
//    }
//
////    public void removeActiveUser(UUID userId) {
////        redisTemplate.opsForSet().remove(ACTIVE_USERS_KEY, userId.toString());
////    }
////
////    public boolean isUserActive(UUID userId) {
////        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ACTIVE_USERS_KEY, userId.toString()));
////    }
//
//    public  Set<Object> getAllActiveUsers() {
//        return Collections.singleton(redisTemplate.opsForSet().members(ACTIVE_USERS_KEY));
//    }

public void addActiveUser(UUID userId, String sessionId) {
    redisTemplate.opsForHash().put(ACTIVE_USERS_KEY, sessionId, userId.toString());
}

    public void removeActiveUser(String sessionId) {
        redisTemplate.opsForHash().delete(ACTIVE_USERS_KEY, sessionId);
    }

    public boolean isUserActive(String sessionId) {
        return redisTemplate.opsForHash().hasKey(ACTIVE_USERS_KEY, sessionId);
    }

    public String getUserIdBySession(String sessionId) {
        Object userId = redisTemplate.opsForHash().get(ACTIVE_USERS_KEY, sessionId);
        return userId != null ? userId.toString() : null;
    }

    public Map<Object, Object> getAllActiveUsers() {
        return redisTemplate.opsForHash().entries(ACTIVE_USERS_KEY);
    }
}

