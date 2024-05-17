package com.sai.Product.Controller.Sold_Quantity;


import com.sai.Product.Request.BuyRequest;
import com.sai.Product.Entity.Product;
import com.sai.Product.Service.Sold.Sold_Quantity_Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Products")
public class Sold_Quantity_Controller {

    private final Sold_Quantity_Service soldQuantityService;

    public Sold_Quantity_Controller(Sold_Quantity_Service soldQuantityService) {
        this.soldQuantityService = soldQuantityService;
    }


    @PostMapping("/updateSoldQuantity")
    // http://localhost:8080/Products/updateSoldQuantity
    public ResponseEntity<String> updateSoldQuantity(@RequestBody BuyRequest buyRequest) {
        try {
            soldQuantityService.updateSoldQuantities(buyRequest.getProductName(), buyRequest.getQuantity());
            return ResponseEntity.ok("Updated sold quantity for " + buyRequest.getProductName());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/resetSoldQuantities")
    // http://localhost:8080/Products/resetSoldQuantities
    public ResponseEntity<String> resetSoldQuantities() {
        soldQuantityService.resetAllSoldQuantities();
        return ResponseEntity.ok("All sold quantities have been reset.");
    }


    @PostMapping("/resetSoldQuantity/{productName}")
    // http://localhost:8080/Products/resetSoldQuantity/{productName}
    public ResponseEntity<?> resetSoldQuantity(@PathVariable String productName) {
        try {
            boolean result = soldQuantityService.resetSoldQuantity(productName);
            if (result) {
                return ResponseEntity.ok("Sold quantity reset successfully for " + productName);
            } else {
                return ResponseEntity.badRequest().body("Product not found");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/products/soldQuantity/{productName}")
    // http://localhost:8080/Products/products/soldQuantity/{productName}
    public ResponseEntity<Map<String, Object>> getSoldQuantityOfProduct(@PathVariable String productName) {
        try {
            Integer soldQuantity = soldQuantityService.getSoldQuantityOfProduct(productName);
            if (soldQuantity == null) {
                return ResponseEntity.notFound().build();
            }
            Map<String, Object> response = new HashMap<>();
            response.put("ProductName", productName);
            response.put("SoldQuantity", soldQuantity);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/products/totalSoldQuantity")
    // http://localhost:8080/Products/products/totalSoldQuantity
    public ResponseEntity<Map<String, Object>> getTotalSoldQuantityOfAllProducts() {
        List<Product> products = soldQuantityService.getAllProducts();
        Map<String, Object> response = new HashMap<>();
        Map<String, Integer> productQuantities = new HashMap<>();
        int totalQuantity = 0;

        for (Product product : products) {
            productQuantities.put(product.getName(), product.getSoldQuantity());
            totalQuantity += product.getSoldQuantity();
        }

        // Organizing the data into the response map
        response.put("Products", productQuantities);
        response.put("Total Sold Quantity", totalQuantity);

        return ResponseEntity.ok(response);
    }


}
