package com.example.petshop.dto;

public class ZaloPayCreateRequest {
    private int orderId;

    public ZaloPayCreateRequest(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
