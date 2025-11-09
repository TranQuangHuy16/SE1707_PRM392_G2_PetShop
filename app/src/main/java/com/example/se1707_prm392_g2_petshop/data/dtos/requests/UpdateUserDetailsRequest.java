package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class UpdateUserDetailsRequest {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String imgUrl;
    public UpdateUserDetailsRequest(String username, String fullName, String email, String phone, String imgUrl) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.imgUrl = imgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
