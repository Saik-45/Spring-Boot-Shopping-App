package com.sai.Product.Request;

public class ProductSoldDTO {
    private String productName;
    private Long quantitySold;

    public ProductSoldDTO(String productName, Integer quantitySold) {
        this.productName = productName;
        this.quantitySold = Long.valueOf(quantitySold);
    }

    // Getters and setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Long quantitySold) {
        this.quantitySold = quantitySold;
    }
}
