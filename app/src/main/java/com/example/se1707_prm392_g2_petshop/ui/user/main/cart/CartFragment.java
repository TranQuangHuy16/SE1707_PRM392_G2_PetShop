package com.example.se1707_prm392_g2_petshop.ui.user.main.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.CartAdapter;
import com.example.se1707_prm392_g2_petshop.data.models.Cart;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentCartBinding;

public class CartFragment extends Fragment implements CartContract.View {

    private FragmentCartBinding binding;
    private CartPresenter presenter;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.applySystemBarInsets(root);

        presenter = new CartPresenter(this, requireContext());

        setupRecyclerView();
        setupListeners();
        presenter.loadCart();

        return root;
    }

    private void setupRecyclerView() {
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(new CartAdapter.OnCartItemClickListener() {
            @Override
            public void onIncreaseClick(com.example.se1707_prm392_g2_petshop.data.models.CartItem cartItem) {
                presenter.increaseQuantity(cartItem.getCartItemId(), cartItem.getQuantity());
            }

            @Override
            public void onDecreaseClick(com.example.se1707_prm392_g2_petshop.data.models.CartItem cartItem) {
                presenter.decreaseQuantity(cartItem.getCartItemId(), cartItem.getQuantity());
            }

            @Override
            public void onRemoveClick(com.example.se1707_prm392_g2_petshop.data.models.CartItem cartItem) {
                presenter.removeItem(cartItem.getCartItemId());
            }
        });
        binding.rvCartItems.setAdapter(cartAdapter);
    }

    private void setupListeners() {
        binding.btnCheckout.setOnClickListener(v -> {
            // Navigate to checkout activity
            android.content.Intent intent = new android.content.Intent(getContext(), 
                com.example.se1707_prm392_g2_petshop.ui.checkout.CheckoutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvCartItems.setVisibility(View.GONE);
        binding.layoutCartSummary.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showCart(Cart cart) {
        binding.rvCartItems.setVisibility(View.VISIBLE);
        binding.layoutCartSummary.setVisibility(View.VISIBLE);
        binding.tvEmptyCart.setVisibility(View.GONE);

        cartAdapter.submitList(cart.getCartItems());
        binding.tvTotalItems.setText(String.valueOf(cart.getTotalItems()));
        binding.tvTotalAmount.setText(String.format("$ %.2f", cart.getTotalAmount()));
    }

    @Override
    public void showEmptyCart() {
        binding.rvCartItems.setVisibility(View.GONE);
        binding.layoutCartSummary.setVisibility(View.GONE);
        binding.tvEmptyCart.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartUpdated() {
        Toast.makeText(getContext(), "Cart updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
