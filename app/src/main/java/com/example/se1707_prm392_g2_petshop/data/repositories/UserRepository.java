package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.se1707_prm392_g2_petshop.data.api.ChatApi;
import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateUserDetailsRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateUserRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static volatile UserRepository instance;
    private UserApi userApi;
    public UserRepository(Context context) {
        this.userApi = RetrofitClient.getUserApi(context.getApplicationContext());
    }

    // Singleton
    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository(context);
                }
            }
        }
        return instance;
    }

    public Call<User> getUserById(int userId) {
        return userApi.getUserById(userId);
    }
    public LiveData<List<User>> getAllUser() {
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        userApi.getAllUsers().enqueue(new Callback<List<User>>(){
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public void updateFcmToken(int userId, String fcmToken) {
        userApi.updateFcmToken(userId, fcmToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("FCM", "Token updated successfully");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FCM", "Failed to update token", t);
            }
        });
    }

    public LiveData<UserDetailResponse> getUserDetail(int userId) {
        MutableLiveData<UserDetailResponse> data = new MutableLiveData<>();
        userApi.getUserDetail(userId).enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserDetailResponse> updateUser(int userId, UpdateUserRequest request) {
        MutableLiveData<UserDetailResponse> data = new MutableLiveData<>();
        userApi.updateUser(userId, request).enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserDetailResponse> updateUserDetails(int userId, UpdateUserDetailsRequest request) {
        MutableLiveData<UserDetailResponse> data = new MutableLiveData<>();
        userApi.updateUserDetails(userId, request).enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
