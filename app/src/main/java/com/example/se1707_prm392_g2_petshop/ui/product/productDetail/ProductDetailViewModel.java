package com.example.se1707_prm392_g2_petshop.ui.product.productDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;

public class ProductDetailViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private MutableLiveData<Integer> productId = new MutableLiveData<>();
    private LiveData<Product> productDetails;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        productRepository = ProductRepository.getInstance(application);

        productDetails = Transformations.switchMap(productId, id ->
                productRepository.getProductById(id)
        );
    }

    public void loadProduct(int id) {
        productId.setValue(id);
    }

    public LiveData<Product> getProductDetails() {
        return productDetails;
    }
}
