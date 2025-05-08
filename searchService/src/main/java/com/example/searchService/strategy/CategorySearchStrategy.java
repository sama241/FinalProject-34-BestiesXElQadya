package com.example.searchService.strategy;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CategorySearchStrategy implements SearchStrategy {

    private final WorkerClient workerClient;

    @Autowired
    public CategorySearchStrategy(WorkerClient workerClient) {
        this.workerClient = workerClient;
    }

    @Override
    public List<Map<String, Object>> search(SearchRequest request) {
        // Fetch all workers from the WorkerClient
        List<Map<String, Object>> workers = workerClient.getWorkers();

        // Filter workers by profession/category
        return workers.stream()
                .filter(worker -> worker.get("profession") != null && worker.get("profession").equals(request.getCategory()))
                .map(this::extractWorkerDetails)  // map to exclude id and password
                .collect(Collectors.toList());
    }

    private Map<String, Object> extractWorkerDetails(Map<String, Object> worker) {
        Map<String, Object> filteredWorker = new HashMap<>(worker);
        filteredWorker.remove("id");  // Remove the worker ID
        filteredWorker.remove("password");  // Remove the password
        return filteredWorker;
    }
}

