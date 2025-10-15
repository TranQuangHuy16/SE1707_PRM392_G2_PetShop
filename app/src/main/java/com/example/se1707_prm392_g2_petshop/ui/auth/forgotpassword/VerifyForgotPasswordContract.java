package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

public interface VerifyForgotPasswordContract {
    interface View {
        void showMessage(String message);
        void navigateToReset(String email);
    }

    interface Presenter {
        void verifyOtp(String email, String otp);
        void requestOtp(String email);
    }
}
