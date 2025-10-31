package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.se1707_prm392_g2_petshop.data.api.CartApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.AddToCartRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateCartItemRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Cart;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private static volatile CartRepository instance;
    private CartApi cartApi;

    private CartRepository(Context context) {
        this.cartApi = RetrofitClient.getCartApi(context.getApplicationContext());
    }

    // Singleton
    public static CartRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (CartRepository.class) {
                if (instance == null) {
                    instance = new CartRepository(context);
                }
            }
        }
        return instance;
    }

    public LiveData<Cart> getMyCart() {
        MutableLiveData<Cart> data = new MutableLiveData<>();
        cartApi.getMyCart().enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Cart> addToCart(AddToCartRequest request) {
        MutableLiveData<Cart> data = new MutableLiveData<>();
        cartApi.addToCart(request).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Boolean> updateCartItem(int cartItemId, UpdateCartItemRequest request) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        cartApi.updateCartItem(cartItemId, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                data.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                data.setValue(false);
            }
        });
        return data;
    }

    public LiveData<Boolean> removeCartItem(int cartItemId) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        cartApi.removeCartItem(cartItemId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                data.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                data.setValue(false);
            }
        });
        return data;
    }

    public LiveData<Boolean> clearCart() {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        cartApi.clearCart().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                data.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                data.setValue(false);
            }
        });
        return data;
    }
}
