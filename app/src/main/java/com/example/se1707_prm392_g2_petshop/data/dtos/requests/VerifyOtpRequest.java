package com.example.se1707_prm392_g2_petshop.data.dtos.requests;

public class VerifyOtpRequest {
    private String email;
    private String code;

    public VerifyOtpRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
