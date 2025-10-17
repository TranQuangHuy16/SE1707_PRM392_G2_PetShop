package com.example.se1707_prm392_g2_petshop.data.models;

public class Message {
    private int messageId;
    private int chatRoomId;
    private int senderId;
    private String messageText;
    private String sentAt;

    public Message(int messageId, int chatRoomId, int senderId, String messageText, String sendAt) {
        this.messageId = messageId;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.messageText = messageText;
        this.sentAt = sendAt;
    }

    public Message() {

    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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

    public String getSendAt() {
        return sentAt;
    }

    public void setSendAt(String sendAt) {
        this.sentAt = sendAt;
    }
}
