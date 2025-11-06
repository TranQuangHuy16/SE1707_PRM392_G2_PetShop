package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;

import com.example.se1707_prm392_g2_petshop.data.api.OrderApi;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;


    // Singleton
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.se1707_prm392_g2_petshop.data.api.OrderApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.CreateOrderRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Order;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private static volatile OrderRepository instance;
    private OrderApi orderApi;

    private OrderRepository(Context context) {
        this.orderApi = RetrofitClient.getInstance(context).create(OrderApi.class);
    }

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

    public LiveData<Order> createOrderFromCart(CreateOrderRequest request) {
        MutableLiveData<Order> orderLiveData = new MutableLiveData<>();
        
        orderApi.createOrderFromCart(request).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderLiveData.postValue(response.body());
                } else {
                    orderLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("OrderRepository", "Network error creating order", t);
                orderLiveData.postValue(null);
            }
        });

        return orderLiveData;
    }

    public LiveData<List<Order>> getMyOrders() {
        MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>();
        
        orderApi.getMyOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ordersLiveData.postValue(response.body());
                } else {
                    ordersLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                ordersLiveData.postValue(null);
            }
        });

        return ordersLiveData;
    }

    public LiveData<Order> getOrderById(int orderId) {
        MutableLiveData<Order> orderLiveData = new MutableLiveData<>();
        
        orderApi.getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderLiveData.postValue(response.body());
                } else {
                    orderLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                orderLiveData.postValue(null);
            }
        });

        return orderLiveData;
    }

    public LiveData<List<Order>> getAllOrders(String status) {
        MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>();

        Call<List<Order>> call;

        // ✅ Nếu không có status, gọi API không truyền param
        if (status == null || status.trim().isEmpty()) {
            call = orderApi.getAllOrders(null);  // Retrofit sẽ KHÔNG gửi ?status=
        } else {
            call = orderApi.getAllOrders(status);
        }

        Log.d("OrderRepository", "Gọi API lấy danh sách đơn hàng, status = " + status);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("OrderRepository", "Tải thành công " + response.body().size() + " đơn hàng");
                    ordersLiveData.postValue(response.body());
                } else {
                    Log.e("OrderRepository", "getAllOrders failed: HTTP " + response.code());
                    ordersLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("OrderRepository", "Network error in getAllOrders", t);
                ordersLiveData.postValue(null);
            }
        });

        return ordersLiveData;
    }


    public LiveData<Boolean> cancelOrder(int orderId) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
        
        orderApi.cancelOrder(orderId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                resultLiveData.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultLiveData.postValue(false);
            }
        });

        return resultLiveData;
    }
}
