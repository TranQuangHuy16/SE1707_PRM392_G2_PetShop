package com.example.se1707_prm392_g2_petshop.ui.user.main.home;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.CategoryAdapter;
import com.example.se1707_prm392_g2_petshop.data.adapter.ProductAdapter;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentHomeBinding;
import com.example.se1707_prm392_g2_petshop.ui.chat.ChatActivity;
import com.example.se1707_prm392_g2_petshop.ui.map.MapActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter featuredProductAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        seUpFabChat(binding.fabChat);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        FloatingActionButton fabChat = root.findViewById(R.id.fab_chat);
        FloatingActionButton fabMap = root.findViewById(R.id.fab_map);

        seUpFabChat(fabChat);
        setUpFabMap(fabMap);
        
        setupCategoryRV();
        setupFeaturedProductRV();
        observeViewModel();

        binding.tvViewAllFeatured.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_home_to_product_list_all);
        });

        return root;
    }

    private void seUpFabChat(FloatingActionButton fabChat) {
        fabChat.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        });
    }

    private void setUpFabMap(FloatingActionButton fabMap) {
        fabMap.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapActivity.class);
            startActivity(intent);
        });
    }
    private void setupCategoryRV() {
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(category -> {
            NavController navController = Navigation.findNavController(requireView());

            HomeFragmentDirections.ActionHomeToProductList action =
                    HomeFragmentDirections.actionHomeToProductList(
                            category.getCategoryId(),
                            category.getCategoryName()
                    );

            navController.navigate(action);
        });
        binding.rvCategories.setAdapter(categoryAdapter);
    }

    private void setupFeaturedProductRV() {
        binding.rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        featuredProductAdapter = new ProductAdapter(product -> {
            NavController navController = Navigation.findNavController(requireView());
            HomeFragmentDirections.ActionHomeToProductDetail action =
                    HomeFragmentDirections.actionHomeToProductDetail(product.getProductId());
            navController.navigate(action);
        });

        binding.rvFeaturedProducts.setAdapter(featuredProductAdapter);
    }

    private void observeViewModel() {
        homeViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categoryAdapter.submitList(categories);
            }
        });

        homeViewModel.getFeaturedProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                featuredProductAdapter.submitList(products);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
