package com.example.searchService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "worker-service", url = "http://worker-service:8082")
public interface WorkerClient {

    @GetMapping("/workers")
    List<Map<String, Object>> getWorkers();
}
