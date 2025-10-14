package com.example.se1707_prm392_g2_petshop.ui.user.main.profile;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;

public interface ProfileContract {
    interface View {
        void onLogoutSuccess(String message);
        void onLogoutFailure(String message);
        void onLogoutError(String message);
    }

    interface Presenter {
        void logout();
    }
}
