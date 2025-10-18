package com.example.se1707_prm392_g2_petshop.data.dtos.responses;

public class SendMessageResponse {
    private int messageId;

    public SendMessageResponse(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
