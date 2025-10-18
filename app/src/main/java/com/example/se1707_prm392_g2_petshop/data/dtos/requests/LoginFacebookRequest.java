package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class LoginFacebookRequest {
    private String accessToken;

    public LoginFacebookRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
