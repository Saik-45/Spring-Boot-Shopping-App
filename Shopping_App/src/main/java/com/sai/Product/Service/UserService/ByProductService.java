package com.sai.Product.Service.UserService;

import com.sai.Product.Entity.Product;
import com.sai.Product.Entity.SearchStat;
import com.sai.Product.Repository.ProductRepository;
import com.sai.Product.Repository.SearchStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ByProductService {

    private final ProductRepository productRepository;

    private final SearchStatsRepository searchStatsRepository;

    public ByProductService(ProductRepository productRepository, SearchStatsRepository searchStatsRepository) {
        this.productRepository = productRepository;
        this.searchStatsRepository = searchStatsRepository;
    }

    @Transactional
    public boolean buyProductByName(String productName, int quantityToBuy) {
        try {
            Product product = productRepository.findByName(productName)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productName));

            if (product.getQuantity() >= quantityToBuy) {
                product.setQuantity(product.getQuantity() - quantityToBuy);
                product.setSoldQuantity((product.getSoldQuantity() == null ? 0 : product.getSoldQuantity()) + quantityToBuy);
                productRepository.save(product);
                return true;
            } else {
                throw new IllegalArgumentException("Not enough quantity available for product: " + productName);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the request.", e);
        }
    }


    @Transactional(readOnly = true)
    public Optional<Product> searchProductByName(String productName) {
        Optional<Product> productOpt = productRepository.findByName(productName);
        productOpt.ifPresent(this::incrementSearchCount);
        return productOpt;
    }



    @Transactional
    public void incrementSearchCount(Product product) {
        // Find existing SearchStat or create a new one if not found
        SearchStat searchStat = searchStatsRepository.findByProductName(product)
                .orElse(new SearchStat(product, 0));

        // Increment the count
        searchStat.incrementCount();

        // Save the updated SearchStat
        searchStatsRepository.save(searchStat);
    }

}
