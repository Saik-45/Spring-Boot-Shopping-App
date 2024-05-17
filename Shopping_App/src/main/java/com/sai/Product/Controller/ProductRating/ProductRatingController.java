package com.sai.Product.Controller.ProductRating;

import com.sai.Product.Entity.Product;
import com.sai.Product.Request.RateProductRequest;
import com.sai.Product.Service.ProductRating.ProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Products/Ratings")
public class ProductRatingController {

    @Autowired
    private ProductRatingService productRatingService;

    @PostMapping("/{productName}")
    // http://localhost:8080/Products/Ratings/hp laptop?rating=4.25
    public ResponseEntity<String> postRating(@PathVariable String productName, @RequestParam double rating) {
        if (rating < 0 || rating > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 0 and 5.");
        }

        boolean isPosted = productRatingService.postRating(productName, rating);
        if (isPosted) {
            return ResponseEntity.ok(" Your Rating Added Successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{productName}")
    // http://localhost:8080/Products/Ratings/i phone 15 pro
    public ResponseEntity<?> getProductRating(@PathVariable String productName) {
        return productRatingService.getProductRating(productName)
                .map(product -> {
                    Double avgRating = product.getAvgRating();
                    Long ratingCount = Long.valueOf(product.getNoOfVoted());

                    if (avgRating != null && ratingCount != null) {
                        String response = String.format("\t\t %.1f / 5 \n\n\t\t %d Given", avgRating, ratingCount);
                        return ResponseEntity.ok(response);
                    } else if (ratingCount != null && ratingCount == 0) {
                        return ResponseEntity.ok("No ratings available");
                    } else {
                        return ResponseEntity.ok("Data unavailable");
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/reset/{productName}")
    //  http://localhost:8080/Products/Ratings/reset/{productName}
    public ResponseEntity<?> resetRating(@PathVariable String productName) {
        productRatingService.resetAverageRating(productName);
        return ResponseEntity.ok("Rating has been reset successfully.");
    }

    @PostMapping("/update/{productName}")
    // http://localhost:8080/Products/Ratings/update/{productName}
    public ResponseEntity<?> updateRating(@PathVariable String productName, @RequestBody double rating) {
        if (rating < 0 || rating > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 0 and 5.");
        }
        productRatingService.updateAverageRating(productName, rating);
        return ResponseEntity.ok("Rating updated successfully.");
    }

    @GetMapping("/highest-rated")
    // http://localhost:8080/Products/Ratings/highest-rated
    public ResponseEntity<?> getHighestRatedProduct() {
        Optional<Product> highestRatedProduct = productRatingService.getHighestRatedProduct();
        return highestRatedProduct.map(product -> ResponseEntity.ok(getProductResponse(product))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/lowest-rated")
    // http://localhost:8080/Products/Ratings/lowest-rated
    public ResponseEntity<?> getLowestRatedProduct() {
        Optional<Product> lowestRatedProduct = productRatingService.getLowestRatedProduct();
        return lowestRatedProduct.map(product -> ResponseEntity.ok(getProductResponse(product))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Map<String, Object> getProductResponse(Product product) {
        Long ratingCount = Long.valueOf(product.getNoOfVoted());
        String ratingCountDisplay = ratingCount != null ? String.valueOf(ratingCount) : "N/A";

        return Map.of(
                "productName", product.getName(),
                "rating", product.getAvgRating() != null ? String.format("%.2f", product.getAvgRating()) : "N/A",
                "ratingCount", ratingCountDisplay
        );
    }


}
