package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateUserRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @GET(ConstantApi.GET_USER_BY_ID)
    Call<User> getUserById(@Path("id") int id);

    @GET(ConstantApi.GET_ALL_USERS)
    Call<List<User>> getAllUsers();

    @PUT("users/{id}/fcm-token")
    Call<Void> updateFcmToken(@Path("id") int id, @Body String fcmToken);

    @GET(ConstantApi.GET_USER_DETAIL)
    Call<UserDetailResponse> getUserDetail(@Path("id") int userId);

    @PUT(ConstantApi.UPDATE_USER)
    Call<UserDetailResponse> updateUser(@Path("id") int userId, @Body UpdateUserRequest updatedUser);

    @DELETE(ConstantApi.DELETE_USER)
    Call<Void> deleteUser(@Path("id") int userId);
}
