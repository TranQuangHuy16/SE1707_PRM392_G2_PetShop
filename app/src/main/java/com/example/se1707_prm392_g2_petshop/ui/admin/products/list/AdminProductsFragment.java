package com.example.se1707_prm392_g2_petshop.ui.admin.products.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.Category;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.CategoryRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;


import java.util.ArrayList;
import java.util.List;

public class AdminProductsFragment extends Fragment implements AdminProductsContract.View {

    private AdminProductsContract.Presenter mPresenter;
    private AdminProductAdapter activeProductsAdapter;
    private AdminProductAdapter inactiveProductsAdapter;
    private ArrayAdapter<String> mCategoryAdapter;
    private HashMap<Integer, String> mCategoryMap = new HashMap<>();
    private Spinner spinnerCategory;
    private RecyclerView rvProducts, rvProductsInactive;
    private FloatingActionButton fabAddProduct;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupPresenter();

        setupViews(view);
        setupRecyclerView();
        setupCategorySpinner();
        
        // ✅ Fix notch & navigation bar - Áp dụng cho ScrollView
        View scrollView = view.findViewById(R.id.scroll_view_admin_products);
        if (scrollView != null) {
            WindowInsetsUtil.applySystemBarInsets(scrollView);
        }
        
        setupListeners();

        mPresenter.start();
    }

    private void setupViews(View view) {
        spinnerCategory = view.findViewById(R.id.spinner_category);
        rvProducts = view.findViewById(R.id.rv_products);
        rvProductsInactive = view.findViewById(R.id.rv_products_inactive);
        fabAddProduct = view.findViewById(R.id.fab_add_product);
        progressBar = view.findViewById(R.id.progress_loading);
    }

    private void setupPresenter() {
        ProductRepository productRepository = ProductRepository.getInstance(requireContext());
        CategoryRepository categoryRepository = CategoryRepository.getInstance(requireContext());
        mPresenter = new AdminProductsPresenter(
                this,
                productRepository,
                categoryRepository);
    }
    private void setupRecyclerView() {
        activeProductsAdapter = new AdminProductAdapter(mPresenter);
        inactiveProductsAdapter = new AdminProductAdapter(mPresenter);

        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProducts.setAdapter(activeProductsAdapter);

        rvProductsInactive.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductsInactive.setAdapter(inactiveProductsAdapter);
    }

    private void setupCategorySpinner(){
        mCategoryAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>());
        mCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(mCategoryAdapter);
    }

    private void setupListeners() {
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = (String) parent.getItemAtPosition(position);
                // Lấy lại categoryId tương ứng từ HashMap
                int selectedId = -1;
                for (Map.Entry<Integer, String> entry : mCategoryMap.entrySet()) {
                    if (entry.getValue().equals(selectedName)) {
                        selectedId = entry.getKey();
                        break;
                    }
                }
                mPresenter.onCategorySelected(selectedId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        fabAddProduct.setOnClickListener(v -> mPresenter.onAddProductClicked());
    }

    @Override
    public void displayProducts(List<Product> activeProducts, List<Product> inactiveProducts) {
        activeProductsAdapter.submitList(activeProducts);
        inactiveProductsAdapter.submitList(inactiveProducts);
    }

    @Override
    public void displayCategories(List<Category> categories) {
        mCategoryAdapter.clear();
        mCategoryMap.clear();

        // Thêm mục "All"
        mCategoryMap.put(-1, "All");
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("All");

        // Duyệt danh sách categories từ backend
        if (categories != null) {
            for (Category category : categories) {
                mCategoryMap.put(category.getCategoryId(), category.getCategoryName());
                categoryNames.add(category.getCategoryName());
            }
        }
        // Cập nhật adapter (chỉ hiển thị tên)
        mCategoryAdapter.addAll(categoryNames);
        mCategoryAdapter.notifyDataSetChanged();
    }



    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void refreshProductList() {
//        // Yêu cầu Presenter tải lại dữ liệu từ category hiện tại
//        String selectedName = (String) spinnerCategory.getSelectedItem();
//        int selectedId = -1; // Default to "All"
//        if (selectedName != null) {
//            for (Map.Entry<Integer, String> entry : mCategoryMap.entrySet()) {
//                if (entry.getValue().equals(selectedName)) {
//                    selectedId = entry.getKey();
//                    break;
//                }
//            }
//        }
//        mPresenter.onCategorySelected(selectedId);
//    }

    @Override
    public void navigateToManageScreen(int productId) {
        AdminProductsFragmentDirections
                .ActionAdminProductsToProductManage action =
        AdminProductsFragmentDirections
                .actionAdminProductsToProductManage(productId);
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
