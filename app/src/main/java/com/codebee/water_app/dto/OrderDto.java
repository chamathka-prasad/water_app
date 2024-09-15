package com.codebee.water_app.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    private Long id;

    private String latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<OrderItemDto> getCartItemsDtos() {
        return cartItemsDtos;
    }

    public void setCartItemsDtos(OrderItemDto cartItemsDtos) {
        this.cartItemsDtos.add(cartItemsDtos);
    }

    private String longitude;

    private List<OrderItemDto> cartItemsDtos = new ArrayList<>();

}
