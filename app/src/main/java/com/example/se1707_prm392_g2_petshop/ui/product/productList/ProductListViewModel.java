package com.example.se1707_prm392_g2_petshop.ui.product.productList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;

import java.util.List;

public class ProductListViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private MutableLiveData<Integer> categoryId = new MutableLiveData<>();
    private LiveData<List<Product>> products;

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        productRepository = ProductRepository.getInstance(application);

        products = Transformations.switchMap(categoryId, id -> {
            if (id == -1) {
                return productRepository.getAllProducts();
            } else {
                return productRepository.getProductsByCategoryId(id);
            }
        });
    }

    public void loadProductsByCategory(int id) {
        categoryId.setValue(id);
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }
}
