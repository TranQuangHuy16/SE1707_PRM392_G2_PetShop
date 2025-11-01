package com.example.se1707_prm392_g2_petshop.ui.user.main.cart;

import com.example.se1707_prm392_g2_petshop.data.models.Cart;

public interface CartContract {
    interface View {
        void showLoading();
        void hideLoading();
        void showCart(Cart cart);
        void showEmptyCart();
        void showError(String message);
        void onCartUpdated();
    }

    interface Presenter {
        void loadCart();
        void increaseQuantity(int cartItemId, int currentQuantity);
        void decreaseQuantity(int cartItemId, int currentQuantity);
        void removeItem(int cartItemId);
        void clearCart();
    }
}
