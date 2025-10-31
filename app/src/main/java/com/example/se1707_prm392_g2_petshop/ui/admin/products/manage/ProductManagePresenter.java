package com.example.se1707_prm392_g2_petshop.ui.admin.products.manage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.CreateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateProductRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.CategoryRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;

import java.util.List;

/**
 * Presenter cho ProductManageFragment
 * - Load chi tiết sản phẩm
 * - Load danh mục
 * - Lưu hoặc xóa sản phẩm
 */
public class ProductManagePresenter implements ProductManageContract.Presenter {

    private final ProductManageContract.View mView;
    private final ProductRepository _productRepo;
    private final CategoryRepository _categoryRepo;

    public ProductManagePresenter(ProductManageContract.View mView,
                                  CategoryRepository categoryRepository,
                                  ProductRepository productRepository) {
        this.mView = mView;
        this._categoryRepo = categoryRepository;
        this._productRepo = productRepository;
    }

    /**
     * Lấy toàn bộ danh mục (LiveData) để View observe.
     */
    @Override
    public LiveData<List<Category>> loadCategories() {
        return _categoryRepo.getAllCategories();
    }

    /**
     * Lấy chi tiết sản phẩm theo ID và trả kết quả về View.
     */
    @Override
    public void loadProductDetail(int productId) {
        _productRepo.getProductById(productId).observeForever(new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                if (product != null) {
                    mView.showProductDetail(product);
                } else {
                    mView.showErrorMessage("Failed to load product detail.");
                }
                // Ngừng observe sau khi nhận được dữ liệu 1 lần
                _productRepo.getProductById(productId).removeObserver(this);
            }
        });
    }

    /**
     * Lưu hoặc cập nhật sản phẩm.
     * Nếu productId = 0 => tạo mới, ngược lại => cập nhật.
     */
    @Override
    public void saveProduct(Product product) {
        if (product == null) {
            mView.showErrorMessage("Invalid product data.");
            return;
        }

        if (product.getProductId() == 0) {
            // Create new product
            CreateProductRequest createProductRequest = new CreateProductRequest(
                    product.getCategoryId(),
                    product.getProductName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getImageUrl(),
                    product.isActive()
            );
            _productRepo.createProduct(createProductRequest).observeForever(success -> {
                if (Boolean.TRUE.equals(success)) {
                    mView.showSuccessMessage("Product created successfully!");
                } else {
                    mView.showErrorMessage("Failed to create product.");
                }
            });
        } else {
            // Update existing product
            UpdateProductRequest updateProductRequest = new UpdateProductRequest(
                    product.getCategoryId(),
                    product.getProductName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getImageUrl(),
                    product.isActive()
            );
            _productRepo.updateProduct(product.productId ,updateProductRequest).observeForever(success -> {
                if (Boolean.TRUE.equals(success)) {
                    mView.showSuccessMessage("Product updated successfully!");
                } else {
                    mView.showErrorMessage("Failed to update product.");
                }
            });
        }
    }

    /**
     * Xóa sản phẩm hiện tại theo ID.
     */
    @Override
    public void deleteProduct(int productId) {
        _productRepo.deleteProduct(productId).observeForever(success -> {
            if (Boolean.TRUE.equals(success)) {
                mView.showSuccessMessage("Product deleted successfully!");
            } else {
                mView.showErrorMessage("Failed to delete product.");
            }
        });
    }
}
