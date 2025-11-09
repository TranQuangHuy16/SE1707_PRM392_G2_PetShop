package com.example.se1707_prm392_g2_petshop.ui.product.productDetail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.ProductRatingAdapter;
import com.example.se1707_prm392_g2_petshop.data.api.ProductApi;
import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.AddToCartRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.ProductRatingRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.ProductRatingResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.models.ProductRating;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.CartRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentProductDetailBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailFragment extends Fragment {

    private FragmentProductDetailBinding binding;
    private ProductDetailViewModel viewModel;
    private CartRepository cartRepository;
    private ProductRatingAdapter ratingAdapter;
    private Product currentProduct;
    private int productId;
    private int quantity = 1;
    private Map<Integer, String> userMap = new HashMap<>();

    // ================== LIFECYCLE ==================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        cartRepository = CartRepository.getInstance(requireContext());

        WindowInsetsUtil.applySystemBarInsets(binding.nestedScrollProductDetail);

        if (getArguments() != null) {
            productId = ProductDetailFragmentArgs.fromBundle(getArguments()).getProductId();
            viewModel.loadProduct(productId);
        }

        // Toolbar back button
        binding.toolbarProductDetail.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        setupQuantityControls();
        setupAddToCartButton();
        setupRatingsRecyclerView();
        setupSubmitRatingButton();
        observeProductDetails();
        loadRatings();

        return binding.getRoot();
    }

    // ================== QUANTITY ==================
    private void setupQuantityControls() {
        updateQuantityDisplay();

        binding.btnIncrease.setOnClickListener(v -> {
            if (currentProduct != null && quantity < currentProduct.getStock()) {
                quantity++;
                updateQuantityDisplay();
            } else {
                Toast.makeText(getContext(), "Số lượng vượt quá tồn kho", Toast.LENGTH_SHORT).show();
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

    // ================== ADD TO CART ==================
    private void setupAddToCartButton() {
        binding.btnAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) addToCart();
        });
    }

    private void addToCart() {
        binding.btnAddToCart.setEnabled(false);
        binding.btnAddToCart.setText("Adding...");

        AddToCartRequest request = new AddToCartRequest(currentProduct.getProductId(), quantity);

        cartRepository.addToCart(request).observe(getViewLifecycleOwner(), cart -> {
            binding.btnAddToCart.setEnabled(true);
            binding.btnAddToCart.setText("Add To Cart");

            if (cart != null) {
                Toast.makeText(getContext(),
                        String.format("Đã thêm %d %s vào giỏ hàng", quantity, currentProduct.getProductName()),
                        Toast.LENGTH_SHORT).show();
                quantity = 1;
                updateQuantityDisplay();
            } else {
                Toast.makeText(getContext(), "Thêm vào giỏ thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================== RATING LIST ==================
    private void setupRatingsRecyclerView() {
        ratingAdapter = new ProductRatingAdapter(new ArrayList<>());
        binding.rvRatings.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRatings.setAdapter(ratingAdapter);
    }

    private void loadRatings() {
        ProductApi productApi = RetrofitClient.getProductApi(requireContext());
        UserApi userApi = RetrofitClient.getUserApi(requireContext());

        productApi.getProductRatings(productId).enqueue(new Callback<ProductRatingResponse>() {
            @Override
            public void onResponse(Call<ProductRatingResponse> call, Response<ProductRatingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductRating> ratings = response.body().getRatings();
                    ratingAdapter.setRatings(ratings);

                    // ===== Tính trung bình =====
                    double sum = 0;
                    for (ProductRating r : ratings) {
                        sum += r.getStars();
                    }
                    double avg = ratings.isEmpty() ? 0 : sum / ratings.size();

                    // Hiển thị
                    if (!ratings.isEmpty()) {
                        binding.tvAverageRating.setText(
                                String.format("⭐ %.1f / 5.0 (%d đánh giá)", avg, ratings.size())
                        );
                    } else {
                        binding.tvAverageRating.setText("⭐ Chưa có đánh giá");
                    }

                    // ===== Load tên user từng rating =====
                    for (ProductRating rating : ratings) {
                        int userId = rating.getUserId();
                        if (!userMap.containsKey(userId)) {
                            userApi.getUserById(userId).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> userResponse) {
                                    if (userResponse.isSuccessful() && userResponse.body() != null) {
                                        String userName = userResponse.body().getFullName();
                                        userMap.put(userId, userName);
                                        ratingAdapter.setUserMap(userMap);
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    // nếu fail, để mặc định hiển thị userId
                                }
                            });
                        }
                    }

                } else {
                    binding.tvAverageRating.setText("⭐ Chưa có đánh giá");
                }
            }

            @Override
            public void onFailure(Call<ProductRatingResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // ================== SUBMIT RATING ==================
    private void setupSubmitRatingButton() {
        binding.btnSubmitRating.setOnClickListener(v -> {
            String comment = binding.edtComment.getText().toString().trim();
            String starsStr = binding.edtStars.getText().toString().trim();

            if (comment.isEmpty() || starsStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập nhận xét và số sao", Toast.LENGTH_SHORT).show();
                return;
            }

            int stars;
            try {
                stars = Integer.parseInt(starsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Số sao phải là số từ 1 đến 5", Toast.LENGTH_SHORT).show();
                return;
            }

            if (stars < 1 || stars > 5) {
                Toast.makeText(getContext(), "Số sao phải từ 1 đến 5", Toast.LENGTH_SHORT).show();
                return;
            }

            //  Lấy userId sau khi đăng nhập
//            SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
//            int userId = prefs.getInt("user_id", -1);
            String id = JwtUtil.getSubFromToken(requireContext());
            int userId = id != null ? Integer.parseInt(id) : -1;
            if (userId == -1) {
                Toast.makeText(getContext(), "Vui lòng đăng nhập để đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }

            ProductRatingRequest request = new ProductRatingRequest(userId, stars, comment);
            ProductApi productApi = RetrofitClient.getProductApi(requireContext());

            productApi.addProductRating(productId, request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Cảm ơn bạn đã đánh giá sản phẩm!", Toast.LENGTH_SHORT).show();
                        binding.edtComment.setText("");
                        binding.edtStars.setText("");
                        loadRatings(); // Refresh lại danh sách
                    } else {
                        Toast.makeText(getContext(), "Gửi đánh giá thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // ================== PRODUCT DETAIL ==================
    private void observeProductDetails() {
        viewModel.getProductDetails().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                currentProduct = product;

                binding.tvProductNameDetail.setText(product.getProductName());
                binding.tvProductPriceDetail.setText(String.format("$ %.2f", product.getPrice()));
                binding.tvProductDescription.setText(product.getDescription());

                Glide.with(requireContext())
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.product_img)
                        .into(binding.imgProductDetail);

                binding.tvStock.setText(String.format("Còn lại: %d sản phẩm", product.getStock()));
                binding.chipCategory.setText(product.getCategoryName());

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
