package com.example.searchService.service;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import com.example.searchService.strategy.SearchStrategy;
import com.example.searchService.strategy.SearchStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private final WorkerClient workerClient;
    private final SearchStrategyFactory searchStrategyFactory;

    @Autowired
    public SearchService(WorkerClient workerClient, SearchStrategyFactory searchStrategyFactory) {
        this.workerClient = workerClient;
        this.searchStrategyFactory = searchStrategyFactory;
    }

    public List<Map<String, Object>> searchWorkers(SearchRequest request) {
        // Fetch all workers from the worker service
        List<Map<String, Object>> workers = workerClient.getWorkers();

        // Get the appropriate search strategy based on the request
        SearchStrategy strategy = searchStrategyFactory.getStrategy(request);

        // Apply the strategy (filtering logic)
        return strategy.search(request);
    }
}
