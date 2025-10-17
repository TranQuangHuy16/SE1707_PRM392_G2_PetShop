package com.example.se1707_prm392_g2_petshop.data.models;

import java.util.ArrayList;

public class Chat {
    private int chatRoomId;
    private int customerId;
    private int adminId;
    private String createdAt;
    private boolean isActive;
    private ArrayList<Message> messages;

    public Chat(int chatRoomId, int customerId, int adminId, String createAt, boolean isActive, ArrayList<Message> messages) {
        this.chatRoomId = chatRoomId;
        this.customerId = customerId;
        this.adminId = adminId;
        this.createdAt = createAt;
        this.isActive = isActive;
        this.messages = messages;
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

    public String getCreateAt() {
        return createdAt;
    }

    public void setCreateAt(String createAt) {
        this.createdAt = createAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
