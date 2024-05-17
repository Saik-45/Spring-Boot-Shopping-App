package com.sai.Product.Service.ProductRating;

import com.sai.Product.Entity.Product;
import com.sai.Product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
public class ProductRatingService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductRatingService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean postRating(String productName, double rating) {
        Optional<Product> productOptional = productRepository.findByName(productName);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            Integer currentVotedCount = product.getNoOfVoted() != null ? product.getNoOfVoted() : 0;
            double currentAvgRating = product.getAvgRating() != null ? product.getAvgRating() : 0.0;

            int newVotedCount = currentVotedCount + 1;
            double newAvgRating = ((currentAvgRating * currentVotedCount) + rating) / newVotedCount;

            product.setAvgRating(newAvgRating);
            product.setNoOfVoted(newVotedCount);
            productRepository.save(product);
            return true;
        }
        return false;
    }



    public Optional<Product> getProductRating(String productName) {
        return productRepository.findByName(productName);
    }


    public void resetAverageRating(String productName) {
        Optional<Product> productOptional = productRepository.findByName(productName);
        productOptional.ifPresent(product -> {
            product.setAvgRating(0.0);
            product.setNoOfVoted(0);
            productRepository.save(product);
        });
    }

    public void updateAverageRating(String productName, double newRating) {
        Optional<Product> productOptional = productRepository.findByName(productName);
        productOptional.ifPresent(product -> {
            double currentAvgRating = product.getAvgRating();
            int currentVotedCount = product.getNoOfVoted();

            // Calculate the new average rating
            double newAvgRating = ((currentAvgRating * currentVotedCount) + newRating) / (currentVotedCount + 1);

            // Update product with new average rating and voted count
            product.setAvgRating(newAvgRating);
            product.setNoOfVoted(currentVotedCount + 1);
            productRepository.save(product);
        });
    }

    public Optional<Product> getHighestRatedProduct() {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getAvgRating() != null && product.getNoOfVoted() != null && product.getNoOfVoted() > 0) // Ensure valid rating exists
                .max(Comparator.comparing(Product::getAvgRating));
    }

    public Optional<Product> getLowestRatedProduct() {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getAvgRating() != null && product.getNoOfVoted() != null && product.getNoOfVoted() > 0) // Ensure valid rating exists
                .min(Comparator.comparing(Product::getAvgRating));
    }



}
