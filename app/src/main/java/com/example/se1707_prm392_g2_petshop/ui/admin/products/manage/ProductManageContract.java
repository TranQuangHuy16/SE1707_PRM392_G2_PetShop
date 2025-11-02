package com.example.se1707_prm392_g2_petshop.ui.admin.products.manage;

import androidx.lifecycle.LiveData;

import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.data.models.Product;

import java.util.List;

public interface ProductManageContract {
    interface View {
        void showProductDetail(Product product);
        void showSuccessMessage(String s);
        void showErrorMessage(String s);
        void showUploadProgress(boolean isUploading);
        void onImageUploadSuccess(String imageUrl);
        void onImageUploadError(String error);
    }
    interface Presenter {
        LiveData<List<Category>> loadCategories();
        void loadProductDetail(int productId);
        void saveProduct(Product product);
        void deleteProduct(int productId);
    }
}
