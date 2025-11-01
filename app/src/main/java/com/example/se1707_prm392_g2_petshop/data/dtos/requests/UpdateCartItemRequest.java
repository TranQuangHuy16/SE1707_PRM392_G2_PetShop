package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class UpdateCartItemRequest {
    private int quantity;

    public UpdateCartItemRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
