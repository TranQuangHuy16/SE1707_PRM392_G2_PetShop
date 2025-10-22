package com.example.se1707_prm392_g2_petshop.ui.user.main.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.CategoryRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    private LiveData<List<Category>> allCategories;
    private LiveData<List<Product>> featuredProducts;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = CategoryRepository.getInstance(application);
        productRepository = ProductRepository.getInstance(application);

        allCategories = categoryRepository.getAllCategories();
        featuredProducts = productRepository.getAllProducts();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Product>> getFeaturedProducts() {
        return featuredProducts;
    }
}
