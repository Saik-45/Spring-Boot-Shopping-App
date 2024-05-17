package com.sai.Product.Service.Product_Quantites;


import com.sai.Product.Entity.Product;
import com.sai.Product.Repository.ProductRepository;
import com.sai.Product.Repository.SearchStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class Product_Available_Quantity_Service {

    private final ProductRepository productRepository;

    private final SearchStatsRepository searchStatsRepository;

    public Product_Available_Quantity_Service(ProductRepository productRepository, SearchStatsRepository searchStatsRepository) {
        this.productRepository = productRepository;
        this.searchStatsRepository = searchStatsRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public boolean updateProductQuantity(String productName, int additionalQuantity) {
        try {
            Product product = productRepository.findByName(productName)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productName));

            product.setQuantity(product.getQuantity() + additionalQuantity);
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error updating product quantity", e);
        }
    }

    public Map<String, Object> getAllProductQuantities() {
        List<Product> products = productRepository.findAll();
        Map<String, Integer> productQuantities = new HashMap<>();
        int totalQuantity = 0;

        for (Product product : products) {
            productQuantities.put(product.getName(), product.getQuantity());
            totalQuantity += product.getQuantity();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("Products", productQuantities);
        response.put("TotalQuantity", totalQuantity);

        return response;
    }


    public Integer getProductQuantityByName(String productName) {
        Optional<Product> productOpt = productRepository.findByName(productName);
        return productOpt.map(Product::getQuantity).orElse(null);
    }


}
