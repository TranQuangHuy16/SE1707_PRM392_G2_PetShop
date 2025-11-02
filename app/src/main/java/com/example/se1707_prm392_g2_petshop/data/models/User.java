package com.example.se1707_prm392_g2_petshop.data.models;

import com.example.se1707_prm392_g2_petshop.data.models.ModelEnums.UserRoleEnum;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String email;
    private String phone;
    private String imgUrl;

    public User(String username, String password, String fullName, String email, String phone, String imgAvatarl) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.imgUrl = imgAvatarl;
    }

    public User(int userId, String username, String password, String fullName, String role, String email, String phone, String imgUrl) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.imgUrl = imgUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getImgAvatarl() {
        return imgUrl;
    }

    public void setImgAvatarl(String imgAvatarl) {
        this.imgUrl = imgAvatarl;
    }
}
