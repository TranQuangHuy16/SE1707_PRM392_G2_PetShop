package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    @GET(ConstantApi.GET_USER_BY_ID)
    Call<User> getUserById(@Path("id") int id);

    @GET(ConstantApi.GET_ALL_USERS)
    Call<List<User>> getAllUsers();
}
