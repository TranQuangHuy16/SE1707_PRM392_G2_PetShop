package com.example.se1707_prm392_g2_petshop.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Cart {
    @SerializedName("cartId")
    private int cartId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("cartItems")
    private List<CartItem> cartItems;

    @SerializedName("totalAmount")
    private double totalAmount;

    @SerializedName("totalItems")
    private int totalItems;

    public Cart() {
    }

    public Cart(int cartId, int userId, String createdAt, boolean isActive, List<CartItem> cartItems, double totalAmount, int totalItems) {
        this.cartId = cartId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.cartItems = cartItems;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
