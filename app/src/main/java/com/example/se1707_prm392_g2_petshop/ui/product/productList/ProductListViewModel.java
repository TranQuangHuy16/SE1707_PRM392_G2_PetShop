package com.example.se1707_prm392_g2_petshop.ui.product.productList;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.se1707_prm392_g2_petshop.data.api.ProductApi;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final MutableLiveData<Integer> categoryId = new MutableLiveData<>();
    private final LiveData<List<Product>> products;

    //  Thêm biến này để lưu kết quả tìm kiếm
    private final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        productRepository = ProductRepository.getInstance(application);

        // Khi categoryId thay đổi → load lại danh sách sản phẩm
        products = Transformations.switchMap(categoryId, id -> {
            if (id == -1) {
                return productRepository.getAllProducts();
            } else {
                return productRepository.getProductsByCategoryId(id);
            }
        });
    }

    //  Hàm search sản phẩm
    public void searchProducts(String keyword) {
        ProductApi productApi = RetrofitClient.getProductApi(getApplication());
        Call<List<Product>> call = productApi.searchProducts(keyword, null, null, null, null);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadProductsByCategory(int id) {
        categoryId.setValue(id);
    }

    //  Getter cho danh sách sản phẩm bình thường
    public LiveData<List<Product>> getProducts() {
        return products;
    }

    //  Getter cho kết quả search
    public LiveData<List<Product>> getSearchResults() {
        return productsLiveData;
    }
}
