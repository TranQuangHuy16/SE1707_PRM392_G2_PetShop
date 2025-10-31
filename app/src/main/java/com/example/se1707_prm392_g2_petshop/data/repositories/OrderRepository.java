package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;

import com.example.se1707_prm392_g2_petshop.data.api.OrderApi;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

public class OrderRepository {
    private static volatile OrderRepository instance;

    private OrderApi orderApi;

    private OrderRepository(Context context) {
//        this.orderApi = RetrofitClient.getOrderApi(context.getApplicationContext());
    }

    // Singleton
    public static OrderRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (OrderRepository.class) {
                if (instance == null) {
                    instance = new OrderRepository(context);
                }
            }
        }
        return instance;
    }
}
