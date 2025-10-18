package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class SendMessageRequest {
    private int chatRoomId;
    private int senderId;
    private String messageText;
    public SendMessageRequest(int chatRoomId, int senderId, String messageText) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.messageText = messageText;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(int chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
