package com.example.se1707_prm392_g2_petshop.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.se1707_prm392_g2_petshop.data.api.ProductApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private static volatile ProductRepository instance;
    private ProductApi productApi;

    private ProductRepository(Context context) {
        this.productApi = RetrofitClient.getProductApi(context.getApplicationContext());
    }

    // Singleton
    public static ProductRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (ProductRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository(context);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Product>> getAllProducts() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        productApi.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Product>> getAllAdminProducts(Integer categoryId) {
        MutableLiveData<List<Product>> allProductsLiveData = new MutableLiveData<>();

        // 1. Xác định API để gọi cho sản phẩm ACTIVE
        Call<List<Product>> activeCall;
        if (categoryId == null) {
            activeCall = productApi.getAllProducts();
        } else {
            activeCall = productApi.getProductsByCategoryId(categoryId);
        }

        activeCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> activeResponse) {
                List<Product> activeProducts = new ArrayList<>();
                if (activeResponse.isSuccessful() && activeResponse.body() != null) {
                    activeProducts.addAll(activeResponse.body());
                }

                // 2. Xác định API để gọi cho sản phẩm INACTIVE
                Call<List<Product>> inactiveCall;
                if (categoryId == null) {
                    inactiveCall = productApi.getAllProductsNotActive();
                } else {
                    inactiveCall = productApi.getProductsByCategoryIdNotActive(categoryId);
                }

                inactiveCall.enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> inactiveResponse) {
                        List<Product> combinedList = new ArrayList<>(activeProducts);
                        if (inactiveResponse.isSuccessful() && inactiveResponse.body() != null) {
                            // 3. Gộp danh sách inactive vào
                            combinedList.addAll(inactiveResponse.body());
                        }
                        // 4. Cập nhật LiveData với danh sách đã gộp
                        allProductsLiveData.setValue(combinedList);
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        // Nếu gọi API inactive lỗi, vẫn trả về danh sách active đã lấy được
                        allProductsLiveData.setValue(activeProducts);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Nếu gọi API active lỗi ngay từ đầu, trả về null
                allProductsLiveData.setValue(null);
            }
        });

        return allProductsLiveData;
    }

    public LiveData<Product> getProductById(int id) {
        MutableLiveData<Product> data = new MutableLiveData<>();
        productApi.getProductById(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Product>> getProductsByCategoryId(int categoryId) {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        productApi.getProductsByCategoryId(categoryId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<Product> createProduct(CreateProductRequest request) {
        MutableLiveData<Product> data = new MutableLiveData<>();
        productApi.createProduct(request).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<Boolean> updateProduct(int id, UpdateProductRequest request) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        productApi.updateProduct(id, request).enqueue(new Callback<Boolean>() {
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


    public LiveData<Boolean> deleteProduct(int id) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        productApi.deleteProduct(id).enqueue(new Callback<Void>() {
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
