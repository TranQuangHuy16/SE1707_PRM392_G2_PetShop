package com.example.se1707_prm392_g2_petshop.ui.user.main.cart;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateCartItemRequest;
import com.example.se1707_prm392_g2_petshop.data.repositories.CartRepository;

public class CartPresenter implements CartContract.Presenter {
    private CartContract.View view;
    private CartRepository cartRepository;
    private Context context;

    public CartPresenter(CartContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.cartRepository = CartRepository.getInstance(context);
    }

    @Override
    public void loadCart() {
        view.showLoading();
        cartRepository.getMyCart().observe((LifecycleOwner) context, cart -> {
            view.hideLoading();
            if (cart != null && cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
                view.showCart(cart);
            } else {
                view.showEmptyCart();
            }
        });
    }

    @Override
    public void increaseQuantity(int cartItemId, int currentQuantity) {
        UpdateCartItemRequest request = new UpdateCartItemRequest(currentQuantity + 1);
        cartRepository.updateCartItem(cartItemId, request).observe((LifecycleOwner) context, success -> {
            if (success != null && success) {
                view.onCartUpdated();
                loadCart();
            } else {
                view.showError("Failed to update quantity");
            }
        });
    }

    @Override
    public void decreaseQuantity(int cartItemId, int currentQuantity) {
        if (currentQuantity > 1) {
            UpdateCartItemRequest request = new UpdateCartItemRequest(currentQuantity - 1);
            cartRepository.updateCartItem(cartItemId, request).observe((LifecycleOwner) context, success -> {
                if (success != null && success) {
                    view.onCartUpdated();
                    loadCart();
                } else {
                    view.showError("Failed to update quantity");
                }
            });
        } else {
            removeItem(cartItemId);
        }
    }

    @Override
    public void removeItem(int cartItemId) {
        cartRepository.removeCartItem(cartItemId).observe((LifecycleOwner) context, success -> {
            if (success != null && success) {
                view.onCartUpdated();
                loadCart();
            } else {
                view.showError("Failed to remove item");
            }
        });
    }

    @Override
    public void clearCart() {
        cartRepository.clearCart().observe((LifecycleOwner) context, success -> {
            if (success != null && success) {
                view.onCartUpdated();
                loadCart();
            } else {
                view.showError("Failed to clear cart");
            }
        });
    }
}
