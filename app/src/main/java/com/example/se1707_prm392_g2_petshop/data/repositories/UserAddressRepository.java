package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.se1707_prm392_g2_petshop.data.api.UserAddressApi;
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

    public Call<UserAddress> getAddressDefaultByUserId(int userId) {
        return userAddressApi.getAddressDefaultByUserId(userId);
    }

}
