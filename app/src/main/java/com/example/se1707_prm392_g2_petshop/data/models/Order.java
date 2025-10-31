package com.example.se1707_prm392_g2_petshop.data.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private Integer addressId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private UserAddress address;
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Order() {}

    public Order(int orderId, int userId, Integer addressId, Date orderDate, 
                 double totalAmount, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getStatusDisplay() {
        switch (status) {
            case "Pending":
                return "Chờ thanh toán";
            case "Paid":
                return "Đã thanh toán";
            case "Cancelled":
                return "Đã hủy";
            default:
                return status;
        }
    }
}
