package com.example.searchService.controller;

import com.example.searchService.model.SearchRequest;
import com.example.searchService.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public List<Map<String, Object>> searchWorkers(@RequestBody SearchRequest request) {
        return searchService.searchWorkers(request);
    }
}
