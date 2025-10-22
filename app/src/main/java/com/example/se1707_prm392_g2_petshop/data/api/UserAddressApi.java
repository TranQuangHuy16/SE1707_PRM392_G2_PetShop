package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAddressApi {
    @GET(ConstantApi.GET_USER_ADDRESSES)
    Call<UserAddress> getAddressDefaultByUserId(@Path("userId") int userId);
}
