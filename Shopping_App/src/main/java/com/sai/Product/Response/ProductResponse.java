package com.sai.Product.Response;

import com.sai.Product.Entity.Product;

public class ProductResponse {
    private Product product;
    private String message;

    public ProductResponse(Product product, String message) {
        this.product = product;
        this.message = message;
    }

    // Getters
    public Product getProduct() {
        return product;
    }

    public String getMessage() {
        return message;
    }
}
