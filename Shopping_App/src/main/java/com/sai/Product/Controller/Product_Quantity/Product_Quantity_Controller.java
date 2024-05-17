package com.sai.Product.Controller.Product_Quantity;


import com.sai.Product.Entity.Product;
import com.sai.Product.ProductUpdateRequest.UpdateQuantityRequest;
import com.sai.Product.Service.Product_Quantites.Product_Available_Quantity_Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Products")
public class Product_Quantity_Controller {

    private final Product_Available_Quantity_Service productAvailableQuantityService;

    public Product_Quantity_Controller(Product_Available_Quantity_Service productAvailableQuantityService) {
        this.productAvailableQuantityService = productAvailableQuantityService;
    }

    @PostMapping("/addQuantity")
    public ResponseEntity<?> updateProductQuantity(@RequestBody UpdateQuantityRequest request) {
        try {
            boolean result = productAvailableQuantityService.updateProductQuantity(request.getProductName(), request.getAdditionalQuantity());
            if (result) {
                return ResponseEntity.ok("Quantity Add successfully for " + request.getProductName());
            } else {
                return ResponseEntity.badRequest().body("Product not found");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

//    {
//        "productName": "Example Product",
//            "additionalQuantity": 10
//    }




    @GetMapping("/products/All/quantities")
    public ResponseEntity<Map<String, Object>> getAllProductQuantities() {
        List<Product> products = productAvailableQuantityService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Integer> quantities = new HashMap<>();
        int totalQuantity = 0;

        for (Product product : products) {
            quantities.put(product.getName(), product.getQuantity());
            totalQuantity += product.getQuantity();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("Products", quantities);
        response.put("TotalQuantity", totalQuantity);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/product/quantity/{productName}")
    public ResponseEntity<?> getProductQuantityByName(@PathVariable String productName) {
        Integer productQuantity = productAvailableQuantityService.getProductQuantityByName(productName);

        if (productQuantity == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ProductName", productName);
        response.put("Quantity", productQuantity);

        return ResponseEntity.ok(response);
    }

}
