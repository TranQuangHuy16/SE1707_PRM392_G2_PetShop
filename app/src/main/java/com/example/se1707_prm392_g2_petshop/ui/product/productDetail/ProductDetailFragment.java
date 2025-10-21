package com.example.se1707_prm392_g2_petshop.ui.product.productDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentProductDetailBinding;

public class ProductDetailFragment extends Fragment {

    private FragmentProductDetailBinding binding;
    private ProductDetailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        if (getArguments() != null) {
            int productId = ProductDetailFragmentArgs.fromBundle(getArguments()).getProductId();
            viewModel.loadProduct(productId);
        }
        observeProductDetails();

        return root;
    }

    private void observeProductDetails() {
        viewModel.getProductDetails().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                binding.tvProductNameDetail.setText(product.getProductName());
                binding.tvProductPriceDetail.setText(String.format("$ %.2f", product.getPrice()));
                binding.tvProductDescription.setText(product.getDescription());

                Glide.with(getContext())
                        .load(product.getImageUrl())
                        .into(binding.imgProductDetail);

                binding.tvStock.setText(String.format("In Stock: %d", product.getStock()));
                binding.chipCategory.setText(product.getCategoryName());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}