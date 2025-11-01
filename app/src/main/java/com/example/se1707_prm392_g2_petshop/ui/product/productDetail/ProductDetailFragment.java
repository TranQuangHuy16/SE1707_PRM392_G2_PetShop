package com.example.se1707_prm392_g2_petshop.ui.product.productDetail;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.AddToCartRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.repositories.CartRepository;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentProductDetailBinding;

public class ProductDetailFragment extends Fragment {

    private FragmentProductDetailBinding binding;
    private ProductDetailViewModel viewModel;
    private CartRepository cartRepository;
    private int quantity = 1;
    private Product currentProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        cartRepository = CartRepository.getInstance(requireContext());

        if (getArguments() != null) {
            int productId = ProductDetailFragmentArgs.fromBundle(getArguments()).getProductId();
            viewModel.loadProduct(productId);
        }

        setupQuantityControls();
        setupAddToCartButton();
        observeProductDetails();

        return root;
    }

    private void setupQuantityControls() {
        updateQuantityDisplay();

        binding.btnIncrease.setOnClickListener(v -> {
            if (currentProduct != null && quantity < currentProduct.getStock()) {
                quantity++;
                updateQuantityDisplay();
            } else {
                Toast.makeText(getContext(), "Maximum stock reached", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });
    }

    private void updateQuantityDisplay() {
        binding.tvQuantity.setText(String.format("%02d", quantity));
    }

    private void setupAddToCartButton() {
        binding.btnAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                addToCart();
            }
        });
    }

    private void addToCart() {
        // Disable button to prevent double clicks
        binding.btnAddToCart.setEnabled(false);
        binding.btnAddToCart.setText("Adding...");

        AddToCartRequest request = new AddToCartRequest(currentProduct.getProductId(), quantity);

        cartRepository.addToCart(request).observe(getViewLifecycleOwner(), cart -> {
            // Re-enable button
            binding.btnAddToCart.setEnabled(true);
            binding.btnAddToCart.setText("Add To Cart");

            if (cart != null) {
                Toast.makeText(getContext(), 
                    String.format("Added %d %s to cart", quantity, currentProduct.getProductName()), 
                    Toast.LENGTH_SHORT).show();
                
                // Reset quantity after adding to cart
                quantity = 1;
                updateQuantityDisplay();
            } else {
                Toast.makeText(getContext(), "Failed to add to cart. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeProductDetails() {
        viewModel.getProductDetails().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                currentProduct = product;
                
                binding.tvProductNameDetail.setText(product.getProductName());
                binding.tvProductPriceDetail.setText(String.format("$ %.2f", product.getPrice()));
                binding.tvProductDescription.setText(product.getDescription());

                Glide.with(getContext())
                        .load(product.getImageUrl())
                        .into(binding.imgProductDetail);

                binding.tvStock.setText(String.format("In Stock: %d", product.getStock()));
                binding.chipCategory.setText(product.getCategoryName());

                // Enable/disable add to cart based on stock
                binding.btnAddToCart.setEnabled(product.getStock() > 0);
                if (product.getStock() == 0) {
                    binding.btnAddToCart.setText("Out of Stock");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}