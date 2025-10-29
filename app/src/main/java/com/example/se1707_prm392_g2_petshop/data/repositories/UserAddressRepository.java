package com.example.se1707_prm392_g2_petshop.data.repositories;

import com.example.se1707_prm392_g2_petshop.data.api.UserAddressApi;
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress;

import retrofit2.Call;

public class UserAddressRepository {
    private UserAddressApi userAddressApi;

    public UserAddressRepository(UserAddressApi userAddressApi) {
        this.userAddressApi = userAddressApi;
    }

    public Call<UserAddress> getAddressDefaultByUserId(int userId) {
        return userAddressApi.getAddressDefaultByUserId(userId);
    }

}
