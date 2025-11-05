package com.example.se1707_prm392_g2_petshop.ui.admin.settings;

import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;

public interface AdminSettingsContract {
    interface View {
        void setPresenter(Presenter mPrView);
        void displayAdminInfo(UserDetailResponse adminInfo);
        void showLoadingError(String message);
        void onLogoutSuccess(String message);
        void onLogoutFailure(String message);
        void onLogoutError(String message);
        void navigateToLogin();
    }

    interface Presenter {
        void loadAdminInfo();
        void logout();
    }
}
