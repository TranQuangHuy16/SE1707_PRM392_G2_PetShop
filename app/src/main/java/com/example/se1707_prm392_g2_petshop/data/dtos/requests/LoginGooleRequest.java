package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class LoginGooleRequest {
    private String idToken;

    public LoginGooleRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
