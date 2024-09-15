package com.codebee.water_app.model;





public class OrderItem  {

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Orders getOrder() {
        return Order;
    }

    public void setOrder(Orders order) {
        Order = order;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }


    private Orders Order;

    private CartItem cartItem;

}
