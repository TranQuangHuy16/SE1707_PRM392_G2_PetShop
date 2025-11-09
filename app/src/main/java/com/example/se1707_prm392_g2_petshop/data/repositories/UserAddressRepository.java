package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.se1707_prm392_g2_petshop.data.api.UserAddressApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RouteRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UserAddressRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.RouteResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAddressRepository {
    private static volatile UserAddressRepository instance;
    private UserAddressApi userAddressApi;

    public UserAddressRepository(Context context) {
        this.userAddressApi = RetrofitClient.getInstance(context).create(UserAddressApi.class);
    }

    public static UserAddressRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (UserAddressRepository.class) {
                if (instance == null) {
                    instance = new UserAddressRepository(context);
                }
            }
        }
        return instance;
    }

    public LiveData<List<UserAddress>> getUserAddresses() {
        MutableLiveData<List<UserAddress>> addressesLiveData = new MutableLiveData<>();
        
        userAddressApi.getUserAddresses().enqueue(new Callback<List<UserAddress>>() {
            @Override
            public void onResponse(Call<List<UserAddress>> call, Response<List<UserAddress>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addressesLiveData.postValue(response.body());
                } else {
                    addressesLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<UserAddress>> call, Throwable t) {
                addressesLiveData.postValue(null);
            }
        });

        return addressesLiveData;
    }

    // Get default address by userId
    public Call<UserAddress> getAddressDefaultByUserId(int userId) {
        return userAddressApi.getAddressDefaultByUserId(userId);
    }

    // Create address
    public LiveData<UserAddress> createUserAddress(UserAddressRequest request) {
        MutableLiveData<UserAddress> result = new MutableLiveData<>();

        userAddressApi.createUserAddress(request).enqueue(new Callback<UserAddress>() {
            @Override
            public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                if (response.isSuccessful()) {
                    result.postValue(response.body());
                } else {
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserAddress> call, Throwable t) {
                result.postValue(null);
            }
        });

        return result;
    }

    // Update address
    public LiveData<UserAddress> updateUserAddress(int id, UserAddressRequest request) {
        MutableLiveData<UserAddress> result = new MutableLiveData<>();

        userAddressApi.updateUserAddress(id, request).enqueue(new Callback<UserAddress>() {
            @Override
            public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                if (response.isSuccessful()) {
                    result.postValue(response.body());
                } else {
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserAddress> call, Throwable t) {
                result.postValue(null);
            }
        });

        return result;
    }

    public LiveData<UserAddress> deleteUserAddress(int id) {
        MutableLiveData<UserAddress> result = new MutableLiveData<>();

        userAddressApi.deleteUserAddress(id).enqueue(new Callback<UserAddress>() {
            @Override
            public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                if (response.isSuccessful()) {
                    result.postValue(response.body());
                } else {
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserAddress> call, Throwable t) {
                result.postValue(null);
            }
        });

        return result;
    }

    // Get admin address
    public Call<UserAddress> getAdminAddresses() {
        return userAddressApi.getAdminAddresses();
    }

    // Get route for map drawing
    public LiveData<RouteResponse> getRouteAddress(RouteRequest request) {
        MutableLiveData<RouteResponse> data = new MutableLiveData<>();

        userAddressApi.getRouteAddress(request).enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                if (response.isSuccessful()) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                data.postValue(null);
            }
        });

        return data;
    }

}
