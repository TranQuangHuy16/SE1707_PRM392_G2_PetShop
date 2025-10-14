package com.example.se1707_prm392_g2_petshop.ui.user.main.profile;

import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;
    private AuthRepository authRepository;

    public ProfilePresenter(ProfileContract.View view, AuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void logout() {
        authRepository.logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onLogoutSuccess("Logout success");
                } else {
                    view.onLogoutFailure("Logout Failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                view.onLogoutError(t.getMessage());
            }
        });
    }
}
