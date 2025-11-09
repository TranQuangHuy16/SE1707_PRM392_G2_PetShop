package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RouteRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UserAddressRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.RouteResponse;
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAddressApi {
    @POST(ConstantApi.CREATE_USER_ADDRESS)
    Call<UserAddress> createUserAddress(@Body UserAddressRequest request);

    @PUT(ConstantApi.UPDATE_USER_ADDRESS)
    Call<UserAddress> updateUserAddress(@Path("id") int id, @Body UserAddressRequest request);

    @DELETE(ConstantApi.DELETE_USER_ADDRESS)
    Call<UserAddress> deleteUserAddress(@Path("id") int id);

    @GET(ConstantApi.GET_ADMIN_ADDRESSES)
    Call<UserAddress> getAdminAddresses();

    @GET(ConstantApi.GET_USER_ADDRESSES)
    Call<UserAddress> getAddressDefaultByUserId(@Path("userId") int userId);
    
    @GET(ConstantApi.GET_ALL_USER_ADDRESSES)
    Call<List<UserAddress>> getUserAddresses();

    @POST(ConstantApi.ROUTE_ADDRESS)
    Call<RouteResponse> getRouteAddress(@Body RouteRequest request);
}
