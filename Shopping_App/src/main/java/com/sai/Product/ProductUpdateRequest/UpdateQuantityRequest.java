package com.sai.Product.ProductUpdateRequest;

public class UpdateQuantityRequest {
    private String productName;
    private int additionalQuantity;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAdditionalQuantity() {
        return additionalQuantity;
    }

    public void setAdditionalQuantity(int additionalQuantity) {
        this.additionalQuantity = additionalQuantity;
    }
}
