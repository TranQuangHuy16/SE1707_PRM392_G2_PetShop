package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RequestOtpRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.VerifyOtpRequest;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyForgotPasswordPresenter implements VerifyForgotPasswordContract.Presenter{
    private VerifyForgotPasswordContract.View view;
    private AuthRepository authRepository;

    public VerifyForgotPasswordPresenter(VerifyForgotPasswordContract.View view, AuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void verifyOtp(String email, String otp) {
        if (otp == null || otp.trim().isEmpty()) {
            view.showMessage("Please enter the verification code");
            return;
        }

        authRepository.forgotPasswordVerify(new VerifyOtpRequest(email, otp)).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    view.navigateToReset(email);
                } else {
                    view.showMessage("Invalid or expired verification code.");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                view.showMessage("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void requestOtp(String email) {
        authRepository.forgotPasswordRequest(new RequestOtpRequest(email))
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                            view.showMessage("Đã gửi lại mã OTP!");
                        } else {
                            view.showMessage("Không thể gửi lại OTP, vui lòng thử lại.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        view.showMessage("Lỗi mạng: " + t.getMessage());
                    }
                });
    }
}
