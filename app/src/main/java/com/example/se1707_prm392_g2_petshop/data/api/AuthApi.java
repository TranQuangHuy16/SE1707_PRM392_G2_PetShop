package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST(ConstantApi.LOGIN)
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST(ConstantApi.LOGIN_GOOGLE)
    Call<AuthResponse> loginByGoogle(@Body LoginGooleRequest request);
}
