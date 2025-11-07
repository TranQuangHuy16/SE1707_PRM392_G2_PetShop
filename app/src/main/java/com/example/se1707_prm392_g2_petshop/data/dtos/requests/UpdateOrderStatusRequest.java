package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class UpdateOrderStatusRequest {
    private int orderId;
    private String status;

    public UpdateOrderStatusRequest(int orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public String getStatus() { return status; }
}
