package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.ResetPasswordRequest;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordPresenter implements ResetPasswordContract.Presenter{
    private ResetPasswordContract.View view;
    private AuthRepository authRepository;

    public ResetPasswordPresenter(ResetPasswordContract.View view, AuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void resetPassword(String email, String password, String confirmPassword) {
        if (password == null || password.length() < 6) {
            view.showMessage("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            view.showMessage("Passwords do not match");
            return;
        }

        authRepository.resetPassword(new ResetPasswordRequest(email, password)).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    view.onResetSuccess();
                } else {
                    view.showMessage("Failed to reset password.");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                view.showMessage("Network error: " + t.getMessage());
            }
        });
    }
}
