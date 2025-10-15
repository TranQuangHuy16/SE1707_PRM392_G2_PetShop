package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RegisterRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RequestOtpRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.ResetPasswordRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.VerifyOtpRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST(ConstantApi.LOGIN)
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST(ConstantApi.LOGIN_GOOGLE)
    Call<AuthResponse> loginByGoogle(@Body LoginGooleRequest request);

    @POST(ConstantApi.REGISTER)
    Call<User> register(@Body RegisterRequest request);

    @POST(ConstantApi.LOGOUT)
    Call<Void> logout();

    @POST(ConstantApi.FORGOTPASSWORD_REQUEST)
    Call<Boolean> forgotPasswordRequest(@Body RequestOtpRequest request);

    @POST(ConstantApi.FORGOTPASSWORD_VERIFY)
    Call<Boolean> forgotPasswordVerify(@Body VerifyOtpRequest request);

    @POST(ConstantApi.RESET_PASSWORD)
    Call<User> resetPassword(@Body ResetPasswordRequest request);

}
