package com.example.userService.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "review-service", url = "http://review-service:8084/reviews")

public interface ReviewClient {
    @GetMapping("/user/{userId}")
    List<Map<String, Object>> getReviewsByUserId(@PathVariable String userId);



}
