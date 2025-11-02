package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

import com.google.gson.annotations.SerializedName;

public class UpdateUserRequest {

    @SerializedName("role")
    private int role; // backend nhận enum → gửi int (0=Admin, 1=Customer)

    @SerializedName("isActive")
    private Boolean isActive;

    public UpdateUserRequest() {}

    public UpdateUserRequest(int role, Boolean isActive) {
        this.role = role;
        this.isActive = isActive;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
