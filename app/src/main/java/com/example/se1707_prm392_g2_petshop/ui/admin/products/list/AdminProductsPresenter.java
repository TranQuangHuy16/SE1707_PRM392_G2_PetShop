package com.example.se1707_prm392_g2_petshop.ui.admin.products.list;

import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.CategoryRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminProductsPresenter implements AdminProductsContract.Presenter {

    private final AdminProductsContract.View mView;
    private final ProductRepository mProductRepository;
    private final CategoryRepository mCategoryRepository;
    private LifecycleOwner mLifecycleOwner; // Cần để observe LiveData

    private Integer mCurrentCategoryId = null;

    public AdminProductsPresenter(AdminProductsContract.View view,
                              ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        mView = view;
        mProductRepository = productRepository;
        mCategoryRepository = categoryRepository;
        this.mLifecycleOwner = (LifecycleOwner) view;
    }

    @Override
    public void start() {
        if (mView == null) return;
        mView.showLoading();
        loadCategories();
        loadProducts(); // Tải sản phẩm lần đầu (tất cả)
    }

    private void loadCategories() {
        mCategoryRepository.getAllCategories().observe(mLifecycleOwner, categories -> {
            if (categories != null && mView != null) {
                mView.displayCategories(categories);
            }
        });
    }

    private void loadProducts() {
        if (mView == null) return;
        mView.showLoading();

        LiveData<List<Product>> liveData = mProductRepository.getAllAdminProducts(mCurrentCategoryId);

        liveData.observe(mLifecycleOwner, products -> {
            if (products != null && mView != null) {
                List<Product> activeList = new ArrayList<>();
                List<Product> inactiveList = new ArrayList<>();
                for (Product product : products) {
                    if (product.isActive()) {
                        activeList.add(product);
                    } else {
                        inactiveList.add(product);
                    }
                }
                mView.displayProducts(activeList, inactiveList);
            } else if (mView != null) {
                mView.showErrorMessage("Failed to load products.");
            }
            if (mView != null) {
                mView.hideLoading();
            }
        });
    }

    @Override
    public void onCategorySelected(Integer categoryId) {
        this.mCurrentCategoryId = (categoryId != null && categoryId == -1) ? null : categoryId;
        loadProducts(); // Tải lại danh sách sản phẩm theo category mới
    }

    @Override
    public void onAddProductClicked() {
        if (mView != null) {
            // Dùng -1 làm giá trị đặc biệt cho việc tạo mới
            mView.navigateToManageScreen(-1);
        }
    }

    @Override
    public void onProductClicked(Product product) {
        mView.navigateToManageScreen(product.getProductId());
    }

    @Override
    public void onSwitchStateChanged(int productId, boolean isActive, CompoundButton buttonView) {
        if (productId == -1) return;
        buttonView.setEnabled(false);
        // Disable to prevent repeated clicks while processing

        // We need to fetch the product details first to build the update request.
        // getProductById returns LiveData, so we must observe it to get the result.
        LiveData<Product> productLiveData = mProductRepository.getProductById(productId);
        productLiveData.observe(mLifecycleOwner, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                // Remove the observer to avoid multiple notifications
                productLiveData.removeObserver(this);

                if (product == null) {
                    if (mView != null) {
                        mView.showErrorMessage("Product not found.");
                    }
                    buttonView.setEnabled(true);
                    buttonView.setChecked(!isActive); // Revert switch state
                    return;
                }

                UpdateProductRequest updateProductRequest = new UpdateProductRequest(
                        product.getCategoryId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getImageUrl(),
                        isActive
                );

                mProductRepository.updateProduct(productId, updateProductRequest).observe(mLifecycleOwner, success -> {
                    if (mView == null) return;
                    buttonView.setEnabled(true);

                    if (success) {
                        mView.showSuccessMessage(isActive ? "Product activated" : "Product deactivated");
                        loadProducts();
                    } else {
                        mView.showErrorMessage("Failed to update status");
                        buttonView.setChecked(!isActive);
                    }
                });
            }
        });
    }

    @Override
    public void onDeleteProductClicked(Product product) {
        mProductRepository.deleteProduct(product.getProductId()).observe(mLifecycleOwner, success -> {
            if (mView == null) return;

            if (success) {
                mView.showSuccessMessage("Product deleted successfully");
                loadProducts(); // Tải lại danh sách
            } else {
                mView.showErrorMessage("Failed to delete product");
            }
        });
    }

}
