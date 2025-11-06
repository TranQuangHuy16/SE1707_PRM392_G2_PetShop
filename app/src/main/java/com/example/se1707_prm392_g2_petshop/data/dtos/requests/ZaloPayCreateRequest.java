package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

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
