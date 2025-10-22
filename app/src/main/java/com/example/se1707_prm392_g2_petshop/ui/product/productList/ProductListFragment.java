package com.example.se1707_prm392_g2_petshop.ui.product.productList;

import android.os.Bundle;
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
import com.example.se1707_prm392_g2_petshop.databinding.FragmentProductListBinding;

public class ProductListFragment extends Fragment {

    private FragmentProductListBinding binding;
    private ProductListViewModel viewModel;
    private ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        setupRecyclerView();

        int categoryId = -1;
        String categoryName = "All Products";

        if (getArguments() != null) {
            categoryId = ProductListFragmentArgs.fromBundle(getArguments()).getCategoryId();
            if (ProductListFragmentArgs.fromBundle(getArguments()).getCategoryName() != null) {
                categoryName = ProductListFragmentArgs.fromBundle(getArguments()).getCategoryName();
            }
        }
            requireActivity().setTitle(categoryName);

            viewModel.loadProductsByCategory(categoryId);

        observeViewModel();
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
