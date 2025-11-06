package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.CreateOrderRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderApi {
    
    @POST(ConstantApi.CREATE_ORDER_FROM_CART)
    Call<Order> createOrderFromCart(@Body CreateOrderRequest request);

    @GET(ConstantApi.GET_ALL)
    Call<List<Order>> getAllOrders(@Query("status") String status);

    @GET(ConstantApi.GET_MY_ORDERS)
    Call<List<Order>> getMyOrders();

    @GET(ConstantApi.GET_ORDER_BY_ID)
    Call<Order> getOrderById(@Path("orderId") int orderId);

    @PUT(ConstantApi.CANCEL_ORDER)
    Call<Void> cancelOrder(@Path("orderId") int orderId);
}
