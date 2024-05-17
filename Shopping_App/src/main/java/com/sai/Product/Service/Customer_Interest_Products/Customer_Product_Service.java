package com.sai.Product.Service.Customer_Interest_Products;


import com.sai.Product.Request.ProductSalesDTO;
import com.sai.Product.Entity.Product;
import com.sai.Product.Repository.ProductRepository;
import com.sai.Product.Repository.SearchStatsRepository;
import com.sai.Product.Request.ProductSearchStatDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Customer_Product_Service {


    private final ProductRepository productRepository;

    private final SearchStatsRepository searchStatsRepository;


    public Customer_Product_Service(ProductRepository productRepository, SearchStatsRepository searchStatsRepository) {
        this.productRepository = productRepository;
        this.searchStatsRepository = searchStatsRepository;
    }


    public List<ProductSearchStatDTO> getMostSearchedProducts() {
        return searchStatsRepository.findMostSearchedProducts();
    }

    public ProductSearchStatDTO getSearchStatForProductName(String productName) {
        return searchStatsRepository.findSearchStatByProductName(productName);
    }


    public List<Product> findTopMostSoldProducts(int limit) {
        return productRepository.findTopMostSoldProducts(PageRequest.of(0, limit));
    }


    public ProductSalesDTO getQuantitySoldByProductName(String productName) {
        Optional<Product> productOptional = productRepository.findByName(productName);
        if (!productOptional.isPresent()) {
            return null; // Or throw a custom exception if that fits your application logic better
        }
        Product product = productOptional.get();
        return new ProductSalesDTO(product.getName(), product.getSoldQuantity());
    }

}
