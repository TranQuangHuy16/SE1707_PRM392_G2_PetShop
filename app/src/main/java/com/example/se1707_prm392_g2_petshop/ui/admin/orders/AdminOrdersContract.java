package com.example.se1707_prm392_g2_petshop.ui.admin.orders;

import com.example.se1707_prm392_g2_petshop.data.models.Order;

import java.util.List;

public interface AdminOrdersContract {

    interface View {
        void setPresenter(Presenter presenter);
        void showLoading();
        void hideLoading();
        void showOrders(List<Order> orders);
        void showError(String message);
        void showSuccessMessage(String message);
    }

    interface Presenter {
        void start();
        void loadAllOrders();
        void onFilterSelected(String status);
        void onCancelOrderClicked(int orderId);
        void onOrderClicked(Order order);
    }
}
