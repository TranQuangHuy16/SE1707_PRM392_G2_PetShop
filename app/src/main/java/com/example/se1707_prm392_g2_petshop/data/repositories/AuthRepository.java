package com.example.se1707_prm392_g2_petshop.data.repositories;

import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RegisterRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import retrofit2.Call;

public class AuthRepository {
    private AuthApi authApi;

    public AuthRepository(AuthApi authApi) {
        this.authApi = authApi;
    }

    public Call<AuthResponse> login(LoginRequest request) {
        return authApi.login(request);
    }

    public Call<AuthResponse> loginByGoogle(LoginGooleRequest request) {
        return authApi.loginByGoogle(request);
    }

    public Call<User> register(RegisterRequest request) {
        return authApi.register(request);
    }

    public Call<Void> logout() {
        return authApi.logout();
    }
}
