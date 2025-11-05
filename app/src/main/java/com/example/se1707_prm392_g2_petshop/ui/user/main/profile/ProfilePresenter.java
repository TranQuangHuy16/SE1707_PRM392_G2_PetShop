package com.example.se1707_prm392_g2_petshop.ui.user.main.profile;

import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;
    private AuthRepository authRepository;
    private UserRepository userRepository;

    public ProfilePresenter(ProfileContract.View view, AuthRepository authRepository, UserRepository userRepository) {
        this.view = view;
        this.authRepository = authRepository;
        this.userRepository = userRepository;

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

    @Override
    public void getUserProfile(int id) {
        userRepository.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onGetUserProfileSuccess(response.body());
                } else {
                    view.onGetUserProfileFailure("Get profile Failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                view.onGetUserProfileFailure(t.getMessage());
            }
        });
    }
}
