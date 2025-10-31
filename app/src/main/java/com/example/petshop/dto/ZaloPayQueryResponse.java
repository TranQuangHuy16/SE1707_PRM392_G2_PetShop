package com.example.petshop.dto;

public class ZaloPayQueryResponse {
    private int returnCode;
    private String returnMessage;
    private boolean isProcessing;
    private int amount;
    private String zpTransId;

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

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setProcessing(boolean processing) {
        isProcessing = processing;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getZpTransId() {
        return zpTransId;
    }

    public void setZpTransId(String zpTransId) {
        this.zpTransId = zpTransId;
    }
}
