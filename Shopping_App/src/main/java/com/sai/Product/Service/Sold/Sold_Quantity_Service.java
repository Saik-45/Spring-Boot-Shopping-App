package com.sai.Product.Service.Sold;


import com.sai.Product.Entity.Product;
import com.sai.Product.Repository.ProductRepository;
import com.sai.Product.Repository.SearchStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class Sold_Quantity_Service {

    private final ProductRepository productRepository;

    private final SearchStatsRepository searchStatsRepository;

    public Sold_Quantity_Service(ProductRepository productRepository, SearchStatsRepository searchStatsRepository) {
        this.productRepository = productRepository;
        this.searchStatsRepository = searchStatsRepository;
    }


    @Transactional
    public boolean updateSoldQuantities(String productName, int additionalQuantity) {
        try {
            Product product = productRepository.findByName(productName)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productName));

            int currentSold = product.getSoldQuantity() != null ? product.getSoldQuantity() : 0;
            product.setSoldQuantity(currentSold + additionalQuantity);
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error updating sold quantities", e);
        }
    }

    @Transactional
    public void resetAllSoldQuantities() {
        try {
            List<Product> allProducts = productRepository.findAll();
            for (Product product : allProducts) {
                product.setSoldQuantity(0);
                productRepository.save(product);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error resetting all sold quantities", e);
        }
    }


    @Transactional
    public boolean resetSoldQuantity(String productName) {
        try {
            Product product = productRepository.findByName(productName)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productName));

            product.setSoldQuantity(0);
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error resetting sold quantity for product: " + productName, e);
        }
    }


    @Transactional(readOnly = true)
    public Integer getSoldQuantityOfProduct(String productName) {
        Optional<Product> productOptional = productRepository.findByName(productName);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return product.getSoldQuantity();
        } else {
            throw new IllegalArgumentException("Product not found: " + productName);
        }
    }

    @Transactional(readOnly = true)
    public Integer getTotalSoldQuantityOfAllProducts() {
        return productRepository.getTotalSoldQuantity();
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}
