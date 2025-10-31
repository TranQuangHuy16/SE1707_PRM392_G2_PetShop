package com.example.se1707_prm392_g2_petshop.ui.admin.settings;

public interface AdminSettingsContract {
    interface View {
        void setPresenter(Presenter mPrView);
        void onLogoutSuccess(String message);
        void onLogoutFailure(String message);
        void onLogoutError(String message);
        void navigateToLogin();
    }

    interface Presenter {
        void logout();
    }
}
