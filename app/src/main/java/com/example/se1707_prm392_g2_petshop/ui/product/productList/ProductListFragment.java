package com.example.se1707_prm392_g2_petshop.ui.product.productList;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.se1707_prm392_g2_petshop.data.adapter.ProductAdapter;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentProductListBinding;

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding binding;
    private ProductListViewModel viewModel;
    private ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        setupRecyclerView();

        //  Lấy categoryId và categoryName từ arguments
        int categoryIdTemp = -1;
        String categoryName = "All Products";

        if (getArguments() != null) {
            categoryIdTemp = ProductListFragmentArgs.fromBundle(getArguments()).getCategoryId();
            if (ProductListFragmentArgs.fromBundle(getArguments()).getCategoryName() != null) {
                categoryName = ProductListFragmentArgs.fromBundle(getArguments()).getCategoryName();
            }
        }

        final int categoryId = categoryIdTemp;
        requireActivity().setTitle(categoryName);

        viewModel.loadProductsByCategory(categoryId);

        observeViewModel();

        // Tìm kiếm sản phẩm theo từ khóa người dùng nhập
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (keyword.length() >= 2) {
                    viewModel.searchProducts(keyword); // Gọi API tìm kiếm
                } else if (keyword.isEmpty()) {
                    viewModel.loadProductsByCategory(categoryId); // Reset danh sách
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //  Fix notch & navigation bar cho RecyclerView
        WindowInsetsUtil.applySystemBarInsets(binding.rvProducts);

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        binding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(product -> {
            NavController navController = Navigation.findNavController(requireView());
            ProductListFragmentDirections.ActionProductListToProductDetail action =
                    ProductListFragmentDirections.actionProductListToProductDetail(product.getProductId());
            navController.navigate(action);
        });
        binding.rvProducts.setAdapter(productAdapter);
    }

    private void observeViewModel() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                productAdapter.submitList(products);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
