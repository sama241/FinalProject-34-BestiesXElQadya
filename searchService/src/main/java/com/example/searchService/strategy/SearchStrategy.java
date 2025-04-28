package com.example.searchService.strategy;

import com.example.searchService.model.SearchRequest;
import java.util.List;

public interface SearchStrategy {
    List<String> search(SearchRequest request);
}
