package com.example.se1707_prm392_g2_petshop.data.models;

import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("paymentId")
    private int paymentId;

    @SerializedName("orderId")
    private int orderId;

    @SerializedName("paymentMethod")
    private int paymentMethod;

    @SerializedName("paymentMethodName")
    private String paymentMethodName;

    @SerializedName("paymentDate")
    private String paymentDate;

    @SerializedName("amount")
    private double amount;

    @SerializedName("paymentStatus")
    private int paymentStatus;

    @SerializedName("paymentStatusName")
    private String paymentStatusName;

    @SerializedName("isActive")
    private boolean isActive;

    public Payment() {
    }

    public Payment(int paymentId, int orderId, int paymentMethod, String paymentMethodName, String paymentDate, double amount, int paymentStatus, String paymentStatusName, boolean isActive) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.paymentMethodName = paymentMethodName;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentStatusName = paymentStatusName;
        this.isActive = isActive;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatusName() {
        return paymentStatusName;
    }

    public void setPaymentStatusName(String paymentStatusName) {
        this.paymentStatusName = paymentStatusName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Payment Method Enum
    public static final int PAYMENT_METHOD_CASH = 0;
    public static final int PAYMENT_METHOD_CREDIT_CARD = 1;
    public static final int PAYMENT_METHOD_MOMO = 2;
    public static final int PAYMENT_METHOD_ZALOPAY = 3;

    // Payment Status Enum
    public static final int PAYMENT_STATUS_SUCCESS = 0;
    public static final int PAYMENT_STATUS_FAILED = 1;
    public static final int PAYMENT_STATUS_PENDING = 2;
}
