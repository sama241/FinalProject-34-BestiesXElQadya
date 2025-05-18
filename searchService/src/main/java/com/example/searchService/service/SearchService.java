package com.example.searchService.service;

import com.example.searchService.client.WorkerClient;
import com.example.searchService.model.SearchRequest;
import com.example.searchService.strategy.SearchStrategy;
import com.example.searchService.strategy.SearchStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    private final WorkerClient workerClient;
    private final SearchStrategyFactory searchStrategyFactory;

    @Autowired
    public SearchService(WorkerClient workerClient, SearchStrategyFactory searchStrategyFactory) {
        this.workerClient = workerClient;
        this.searchStrategyFactory = searchStrategyFactory;
    }

    public List<Map<String, Object>> searchWorkers(SearchRequest request) {
        try {
            logger.info("Received search request: {}", request);

            // Fetch all workers from the worker service
            List<Map<String, Object>> workers = workerClient.getWorkers();
            logger.info("Fetched {} workers from WorkerService", workers.size());

            // Get the appropriate search strategy based on the request
            SearchStrategy strategy = searchStrategyFactory.getStrategy(request);
            logger.info("Applying search strategy: {}", strategy.getClass().getSimpleName());

            // Apply the strategy (filtering logic)
            List<Map<String, Object>> results = strategy.search(request);
            logger.info("Search completed. {} results returned.", results.size());

            return results;
        } catch (Exception e) {
            logger.error("Error occurred during search operation", e);
            throw e;
        }
    }
}
