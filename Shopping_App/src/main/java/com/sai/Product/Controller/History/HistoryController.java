package com.sai.Product.Controller.History;

import com.sai.Product.Entity.SearchHistory;
import com.sai.Product.Service.History_Service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Products/History")
public class HistoryController {

    @Autowired
    private SearchHistoryService searchHistoryService;

    @GetMapping("/product/{name}")
    public SearchHistory getProductsByName(@PathVariable String name) {


        return  searchHistoryService.saveSearchTerm(name);
    }



}

