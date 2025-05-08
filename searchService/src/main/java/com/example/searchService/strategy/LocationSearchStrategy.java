package com.example.searchService.strategy;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LocationSearchStrategy implements SearchStrategy {

    private final WorkerClient workerClient;

    @Autowired
    public LocationSearchStrategy(WorkerClient workerClient) {
        this.workerClient = workerClient;
    }

    @Override
    public List<Map<String, Object>> search(SearchRequest request) {
        // Fetch all workers from the WorkerClient
        List<Map<String, Object>> workers = workerClient.getWorkers();

        // Filter workers by location and exclude 'id' and 'password' fields
        return workers.stream()
                .filter(worker -> worker.get("location") != null && worker.get("location").equals(request.getLocation()))
                .map(worker -> removeSensitiveData(worker)) // Remove 'id' and 'password'
                .collect(Collectors.toList());
    }

    // Method to exclude 'id' and 'password' from the worker map
    private Map<String, Object> removeSensitiveData(Map<String, Object> worker) {
        Map<String, Object> filteredWorker = new HashMap<>(worker);
        filteredWorker.remove("id"); // Remove the 'id' field
        filteredWorker.remove("password"); // Remove the 'password' field
        return filteredWorker;
    }
}
