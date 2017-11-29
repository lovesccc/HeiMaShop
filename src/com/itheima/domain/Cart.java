package com.itheima.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<String,CartItem> cartItem  = new HashMap<>();
    private double totalPrice;

    public Map<String, CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(Map<String, CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
