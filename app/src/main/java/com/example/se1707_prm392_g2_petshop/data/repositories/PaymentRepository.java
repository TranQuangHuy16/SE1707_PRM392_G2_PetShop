package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.se1707_prm392_g2_petshop.data.api.PaymentApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreatePaymentRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Payment;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {
    private static volatile PaymentRepository instance;
    private PaymentApi paymentApi;

    private PaymentRepository(Context context) {
        this.paymentApi = RetrofitClient.getPaymentApi(context.getApplicationContext());
    }

    // Singleton
    public static PaymentRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (PaymentRepository.class) {
                if (instance == null) {
                    instance = new PaymentRepository(context);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Payment>> getMyPayments() {
        return getMyPayments(null);
    }

    public LiveData<List<Payment>> getMyPayments(Integer status) {
        MutableLiveData<List<Payment>> data = new MutableLiveData<>();
        paymentApi.getMyPayments(status).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Payment> getPaymentById(int id) {
        MutableLiveData<Payment> data = new MutableLiveData<>();
        paymentApi.getPaymentById(id).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Payment> getPaymentByOrderId(int orderId) {
        MutableLiveData<Payment> data = new MutableLiveData<>();
        paymentApi.getPaymentByOrderId(orderId).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Payment> createPayment(CreatePaymentRequest request) {
        MutableLiveData<Payment> data = new MutableLiveData<>();
        paymentApi.createPayment(request).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
