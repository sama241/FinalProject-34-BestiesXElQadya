package com.example.searchService.strategy;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AvailabilitySearchStrategy implements SearchStrategy {

    private final WorkerClient workerClient;

    @Autowired
    public AvailabilitySearchStrategy(WorkerClient workerClient) {
        this.workerClient = workerClient;
    }

    @Override
    public List<Map<String, Object>> search(SearchRequest request) {
        // Fetch all workers from the WorkerClient
        List<Map<String, Object>> workers = workerClient.getWorkers();

        // Filter workers based on available hours (if not empty)
        return workers.stream()
                .filter(worker -> worker.containsKey("availableHours") && !((List<Integer>) worker.get("availableHours")).isEmpty())
                .map(this::extractWorkerDetails)  // Exclude worker ID and password
                .collect(Collectors.toList());
    }

    // Method to exclude workerId and password from the response
    private Map<String, Object> extractWorkerDetails(Map<String, Object> worker) {
        Map<String, Object> filteredWorker = new HashMap<>(worker);
        filteredWorker.remove("id");  // Remove the worker ID
        filteredWorker.remove("password");  // Remove the password
        return filteredWorker;
    }
}

