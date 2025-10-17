package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class CreateRoomRequest {
    private int customerId;
    private int adminId;

    public CreateRoomRequest(int customerId, int adminId) {
        this.customerId = customerId;
        this.adminId = adminId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
