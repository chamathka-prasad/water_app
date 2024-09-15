package com.codebee.water_app.dto;

import java.util.List;

public class CartItemsDto {

    private String productName;
    private List<String> images;

    private Double productPrice;

    private int cartItemQty;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public int getCartItemQty() {
        return cartItemQty;
    }

    public void setCartItemQty(int cartItemQty) {
        this.cartItemQty = cartItemQty;
    }
}
