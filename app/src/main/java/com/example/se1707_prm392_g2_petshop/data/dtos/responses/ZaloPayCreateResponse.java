package com.example.se1707_prm392_g2_petshop.data.dtos.responses;

public class ZaloPayCreateResponse {
    private int returnCode;
    private String returnMessage;
    private String orderUrl;
    private String zpTransToken;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public String getZpTransToken() {
        return zpTransToken;
    }

    public void setZpTransToken(String zpTransToken) {
        this.zpTransToken = zpTransToken;
    }
}
