package com.example.se1707_prm392_g2_petshop.ui.userdetail;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateUserDetailsRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;

public interface UserDetailContract {
    interface View {
        void showUserDetails(UserDetailResponse user);
        void showLoading(boolean isLoading);
        void showUpdateSuccess(String message);
        void showError(String message);
    }

    interface Presenter {
        void loadUserDetails(int userId);
        void updateUserDetails(int userId, UpdateUserDetailsRequest request);
        void onDestroy();
    }
}
