package com.example.se1707_prm392_g2_petshop.ui.admin.dashboard;

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
//            OrderRepository orderRepository,
            ProductRepository productRepository)
    {
        this.mView = mView;
        this.userRepo = userRepository;
//        this.orderRepo = orderRepository;
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

        userRepo.getAllUser().observeForever(new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    mView.showTotalUsers(users.size());
                } else {
                    mView.showError("Failed to load users");
                }
                mView.hideLoading();
            }
        });

        productRepo.getAllProducts().observeForever(new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (products != null) {
                    mView.showTotalProducts(products.size());
                } else {
                    mView.showError("Failed to load products");
                }
                mView.hideLoading();
            }
        });

//        orderRepo.getAllOrders().observeForever(new Observer<List<Order>>() {
//            @Override
//            public void onChanged(List<Product> orders) {
//                if (products != null) {
//                    mView.showTotalOrders(orders.size());
//                } else {
//                    mView.showError("Failed to load products");
//                }
//                mView.hideLoading();
//            }
//        }

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
