package com.example.se1707_prm392_g2_petshop.data.dtos.responses;

public class CreateRoomResponse {
    private int chatRoomId;
    private int customerId;
    private int adminId;
    private String createdAt;
    private boolean isActive;

    public CreateRoomResponse(int chatRoomId, int customerId, int adminId, String createdAt, boolean isActive) {
        this.chatRoomId = chatRoomId;
        this.customerId = customerId;
        this.adminId = adminId;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
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
}

