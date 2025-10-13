package com.example.se1707_prm392_g2_petshop.ui.auth.login;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View view;
    private AuthRepository repository;

    public LoginPresenter(LoginContract.View view, AuthRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void login(LoginRequest request) {
        repository.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onLoginSuccess(response.body());
                } else {
                    view.onLoginFailure("Username or password is incorrect");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                view.onLoginError(t.getMessage());
            }
        });
    }

    @Override
    public void loginWithGoogle(LoginGooleRequest request) {
        repository.loginByGoogle(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onLoginGoogleSuccess(response.body());
                } else {
                    view.onLoginFailure("Username or password is incorrect");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                view.onLoginError(t.getMessage());
            }
        });
    }
}
