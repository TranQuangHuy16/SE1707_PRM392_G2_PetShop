package com.example.se1707_prm392_g2_petshop.ui.auth.forgotpassword;

public interface ResetPasswordContract {
    interface View {
        void showMessage(String message);
        void onResetSuccess();
    }

    interface Presenter {
        void resetPassword(String email, String password, String confirmPassword);
    }
}
