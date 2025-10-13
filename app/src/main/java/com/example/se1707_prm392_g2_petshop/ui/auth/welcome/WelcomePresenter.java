package com.example.se1707_prm392_g2_petshop.ui.auth.welcome;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.ui.auth.signup.SignUpContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomePresenter implements WelcomeContract.Presenter {

    private WelcomeContract.View view;
    private AuthRepository repository;

    public WelcomePresenter(WelcomeContract.View view, AuthRepository repository) {
        this.view = view;
        this.repository = repository;
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
