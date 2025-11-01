package com.example.se1707_prm392_g2_petshop.data.dtos;

public class CreateOrderRequest {
    private Integer addressId;
    private String note;

    public CreateOrderRequest() {
        this.note = "";
    }

    public CreateOrderRequest(Integer addressId, String note) {
        this.addressId = addressId;
        this.note = note != null ? note : "";
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
