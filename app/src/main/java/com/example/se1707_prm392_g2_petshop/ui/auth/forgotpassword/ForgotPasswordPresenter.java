package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RequestOtpRequest;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {
    private ForgotPasswordContract.View view;
    private AuthRepository authRepository;

    public ForgotPasswordPresenter(ForgotPasswordContract.View view, AuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void sendForgotPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            view.showMessage("Please enter your email");
            return;
        }

        authRepository.forgotPasswordRequest(new RequestOtpRequest(email)).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    view.navigateToVerify(email);
                } else {
                    view.showMessage("Failed to send verification code. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                view.showMessage("Network error: " + t.getMessage());
            }
        });
    }
}
