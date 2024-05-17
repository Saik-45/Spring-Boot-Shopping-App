package com.sai.Product.Service.History_Service;


import com.sai.Product.Entity.SearchHistory;
import com.sai.Product.Repository.SearchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    @Autowired
    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    public SearchHistory saveSearchTerm(String searchTerm) {
        SearchHistory searchHistory = new SearchHistory(searchTerm, LocalDateTime.now());
        return searchHistoryRepository.save(searchHistory);

    }
}

