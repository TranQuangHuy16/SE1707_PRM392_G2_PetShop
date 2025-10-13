package com.example.se1707_prm392_g2_petshop.ui.auth.signup;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;

public interface SignUpContract {
    interface View {
        void onLoginGoogleSuccess(AuthResponse response);
        void onLoginFailure(String message);
        void onLoginError(String message);
    }

    interface Presenter {
        void loginWithGoogle(LoginGooleRequest request);
    }
}
