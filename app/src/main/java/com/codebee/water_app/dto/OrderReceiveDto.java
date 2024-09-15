package com.codebee.water_app.dto;



import com.codebee.water_app.model.OrderItem;
import com.codebee.water_app.model.Orders;

import java.util.ArrayList;
import java.util.List;

public class OrderReceiveDto {

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {

        this.orderItem.add(orderItem);
    }

    private Orders orders;

    private List<OrderItem> orderItem = new ArrayList<>();

}
