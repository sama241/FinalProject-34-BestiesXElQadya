package com.example.userService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "worker-service", url = "http://worker-service:8082/workers")
public interface WorkerClient {

    // Get Worker by ID
    @GetMapping("/{id}")
    Map<String, Object> getWorkerById(@PathVariable("id") String workerId);
}


