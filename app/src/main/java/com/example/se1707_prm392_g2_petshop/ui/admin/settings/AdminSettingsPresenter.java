package com.example.se1707_prm392_g2_petshop.ui.admin.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LifecycleOwner;

import com.example.se1707_prm392_g2_petshop.data.constants.Constant;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSettingsPresenter implements AdminSettingsContract.Presenter {

    private final AdminSettingsContract.View mView;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final Context context;

    public AdminSettingsPresenter(AdminSettingsContract.View mView, 
                                 AuthRepository authRepository,
                                 UserRepository userRepository,
                                 Context context) {
        this.mView = mView;
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.context = context;
    }

    @Override
    public void loadAdminInfo() {
        // Lấy userId từ JWT token
        String userIdStr = JwtUtil.getUserIdFromToken(context);
        
        if (userIdStr == null) {
            mView.showLoadingError("Cannot get user information");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            
            // Gọi API để lấy thông tin chi tiết
            userRepository.getUserDetail(userId).observe((LifecycleOwner) mView, adminInfo -> {
                if (adminInfo != null) {
                    mView.displayAdminInfo(adminInfo);
                } else {
                    mView.showLoadingError("Failed to load admin information");
                }
                
                // Gỡ bỏ observer
                userRepository.getUserDetail(userId).removeObservers((LifecycleOwner) mView);
            });
            
        } catch (NumberFormatException e) {
            mView.showLoadingError("Invalid user ID");
        }
    }

    @Override
    public void logout() {
        authRepository.logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mView.onLogoutSuccess("Logout success");
                } else {
                    mView.onLogoutFailure("Logout Failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.onLogoutError(t.getMessage());
            }
        });
    }
}
