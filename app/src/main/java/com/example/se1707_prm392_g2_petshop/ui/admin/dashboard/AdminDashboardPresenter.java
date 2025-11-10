package com.example.se1707_prm392_g2_petshop.ui.admin.dashboard;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.se1707_prm392_g2_petshop.data.models.Product;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.OrderRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;

import java.util.List;

public class AdminDashboardPresenter implements AdminDashboardContract.Presenter {
    private final AdminDashboardContract.View mView;
    private UserRepository userRepo;
    private ProductRepository productRepo;
    private OrderRepository orderRepo;

    public AdminDashboardPresenter(
            AdminDashboardContract.View mView,
            UserRepository userRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository)
    {
        this.mView = mView;
        this.userRepo = userRepository;
        this.orderRepo = orderRepository;
        this.productRepo = productRepository;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadDashboardData();
    }

    @Override
    public void loadDashboardData() {
        mView.showLoading();
        final int[] completedTasks = {0}; // Biến đếm tác vụ hoàn thành

        userRepo.getAllUser().observe((LifecycleOwner) mView, users -> {
            if (users != null) {
                mView.showTotalUsers(users.size());
            } else {
                mView.showError("Failed to load users");
            }
            completedTasks[0]++;
            if (completedTasks[0] == 3) { // Nếu cả 3 tác vụ đã xong
                mView.hideLoading();
            }
        });

        productRepo.getAllProducts().observe((LifecycleOwner) mView, products -> {
            if (products != null) {
                mView.showTotalProducts(products.size());
            } else {
                mView.showError("Failed to load products");
            }
            completedTasks[0]++;
            if (completedTasks[0] == 3) { // Nếu cả 3 tác vụ đã xong
                mView.hideLoading();
            }
        });

        orderRepo.getAllOrders(null).observe((LifecycleOwner) mView, orders -> {
            if (orders != null) {
                mView.showTotalOrders(orders.size());
                } else {
                mView.showError("Failed to load orders");
            }
            completedTasks[0]++;
            if (completedTasks[0] == 3) { // Nếu cả 3 tác vụ đã xong
                mView.hideLoading();
            }
        });
    }


    @Override
    public void onManageProductsClicked() {
        mView.navigateToManageProducts();
    }

    @Override
    public void onManageUsersClicked() {
        mView.navigateToManageUsers();
    }

    @Override
    public void onViewOrdersClicked() {
        mView.navigateToViewOrders();
    }
}
