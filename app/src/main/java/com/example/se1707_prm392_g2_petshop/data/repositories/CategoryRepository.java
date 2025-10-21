package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.se1707_prm392_g2_petshop.data.api.CategoryApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreateCategoryRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateCategoryRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private static volatile CategoryRepository instance;
    private CategoryApi categoryApi;

    private CategoryRepository(Context context) {
        this.categoryApi = RetrofitClient.getCategoryApi(context.getApplicationContext());
    }

    public static CategoryRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (CategoryRepository.class) {
                if (instance == null) {
                    instance = new CategoryRepository(context);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Category>> getAllCategories() {
        MutableLiveData<List<Category>> data = new MutableLiveData<>();
        categoryApi.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Category> getCategoryById(int id) {
        MutableLiveData<Category> data = new MutableLiveData<>();
        categoryApi.getCategoryById(id).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<Category> createCategory(CreateCategoryRequest request) {
        MutableLiveData<Category> data = new MutableLiveData<>();
        categoryApi.createCategory(request).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Boolean> updateCategory(int id, UpdateCategoryRequest request) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        categoryApi.updateCategory(id, request).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                data.setValue(response.isSuccessful() && response.body() != null && response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                data.setValue(false);
            }
        });
        return data;
    }

    public LiveData<Boolean> deleteCategory(int id) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        categoryApi.deleteCategory(id).enqueue(new Callback<Void>() {
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
