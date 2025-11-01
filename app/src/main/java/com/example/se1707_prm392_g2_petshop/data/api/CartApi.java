package com.example.se1707_prm392_g2_petshop.data.api;

import com.example.se1707_prm392_g2_petshop.data.constants.ConstantApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.AddToCartRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateCartItemRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Cart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartApi {
    @GET(ConstantApi.GET_MY_CART)
    Call<Cart> getMyCart();

    @POST(ConstantApi.ADD_TO_CART)
    Call<Cart> addToCart(@Body AddToCartRequest request);

    @PUT(ConstantApi.UPDATE_CART_ITEM)
    Call<Void> updateCartItem(@Path("cartItemId") int cartItemId, @Body UpdateCartItemRequest request);

    @DELETE(ConstantApi.REMOVE_CART_ITEM)
    Call<Void> removeCartItem(@Path("cartItemId") int cartItemId);

    @DELETE(ConstantApi.CLEAR_CART)
    Call<Void> clearCart();
}
