package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

public interface ForgotPasswordContract {
    interface View {
        void showMessage(String message);
        void navigateToVerify(String email);
    }

    interface Presenter {
        void sendForgotPassword(String email);
    }
}
