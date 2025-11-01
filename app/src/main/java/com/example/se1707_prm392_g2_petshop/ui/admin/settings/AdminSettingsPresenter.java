package com.example.se1707_prm392_g2_petshop.ui.admin.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.se1707_prm392_g2_petshop.data.constants.Constant;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSettingsPresenter implements AdminSettingsContract.Presenter {

    private final AdminSettingsContract.View mView;
    private AuthRepository authRepository;

    public AdminSettingsPresenter(AdminSettingsContract.View mView,  AuthRepository authRepository) {
        this.mView = mView;
        this.authRepository = authRepository;
    }

    @Override
    public void logout() {
        authRepository.logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {
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
