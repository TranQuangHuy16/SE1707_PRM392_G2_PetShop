package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreateRoomRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.SendMessageRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.CreateRoomResponse;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.SendMessageResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatApi {
    @POST(ConstantApi.CREATE_ROOM)
    Call<CreateRoomResponse> createRoom(@Body CreateRoomRequest request);

    @GET(ConstantApi.GET_ROOM_BY_CUSTOMER_ID)
    Call<Chat> getRoomByCustomerId(@Path("customerId") int customerId);

    @GET(ConstantApi.GET_ROOM_BY_ADMIN_ID)
    Call<ArrayList<Chat>> getRoomByAdminId(@Path("adminId") int adminId);

    @POST(ConstantApi.SEND_MESSAGE)
    Call<SendMessageResponse> sendMessage(@Body SendMessageRequest request);

    @GET(ConstantApi.GET_USER_BY_ID)
    Call<User> getUserById(@Path("id") int id);
}
