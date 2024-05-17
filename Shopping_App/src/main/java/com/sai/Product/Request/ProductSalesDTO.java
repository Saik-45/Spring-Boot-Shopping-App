package com.sai.Product.Request;

public class ProductSalesDTO {
    private String name;
    private int soldQuantity;

    public ProductSalesDTO(String name, int soldQuantity) {
        this.name = name;
        this.soldQuantity = soldQuantity;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}

