package com.example.searchService.service;

import com.example.searchService.model.SearchRequest;
import com.example.searchService.strategy.SearchStrategy;
import com.example.searchService.strategy.SearchStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private final SearchStrategyFactory searchStrategyFactory;

    @Autowired
    public SearchService(SearchStrategyFactory searchStrategyFactory) {
        this.searchStrategyFactory = searchStrategyFactory;
    }

    public List<Map<String, Object>> searchWorkers(SearchRequest request) {
        SearchStrategy strategy = searchStrategyFactory.getStrategy(request);
        return strategy.search(request);
    }
}
