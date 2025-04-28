package com.example.searchService.strategy;

import com.example.searchService.model.SearchRequest;
import java.util.List;
import java.util.Map;

public interface SearchStrategy {
    List<Map<String, Object>> search(SearchRequest request);
}
