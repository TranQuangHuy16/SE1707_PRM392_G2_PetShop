package com.example.se1707_prm392_g2_petshop.ui.auth.login;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginFacebookRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginGooleRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.LoginRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.AuthResponse;
import com.google.android.gms.auth.api.Auth;

public interface LoginContract {
    interface View {
        void onLoginSuccess(AuthResponse response);
        void onLoginGoogleSuccess(AuthResponse response);
        void onLoginFacebookSuccess(AuthResponse response);
        void onLoginFailure(String message);
        void onLoginError(String message);
    }

    interface Presenter {
        void login(LoginRequest request);
        void loginWithGoogle(LoginGooleRequest request);
        void loginWithFacebook(LoginFacebookRequest request);
    }
}
