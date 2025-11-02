package com.example.se1707_prm392_g2_petshop.data.dtos.responses;

import com.example.se1707_prm392_g2_petshop.data.models.UserAddress;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserDetailResponse {

    @SerializedName("userId")
    private int userId;

    @SerializedName("username")
    private String username;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("imgUrl")
    private String imgUrl;

    @SerializedName("role")
    private String role;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("addresses")
    private List<UserAddress> addresses;

    public UserDetailResponse(int userId, String username, String fullName, String email, String phone, String imgUrl, String role, boolean isActive, String createdAt, List<UserAddress> addresses) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.imgUrl = imgUrl;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.addresses = addresses;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<UserAddress> getAddresses() { return addresses; }
    public void setAddresses(List<UserAddress> addresses) { this.addresses = addresses; }
}