package com.example.se1707_prm392_g2_petshop.data.repositories;

import com.example.se1707_prm392_g2_petshop.data.api.ChatApi;
import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import retrofit2.Call;

public class UserRepository {
    private UserApi userApi;
    public UserRepository(UserApi userApi) {
        this.userApi = userApi;
    }

    public Call<User> getUserById(int userId) {
        return userApi.getUserById(userId);
    }
}
