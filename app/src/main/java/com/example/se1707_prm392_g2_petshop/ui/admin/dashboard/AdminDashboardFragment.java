package com.example.se1707_prm392_g2_petshop.ui.admin.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.se1707_prm392_g2_petshop.R;
import com.google.android.material.card.MaterialCardView;

public class AdminDashboardFragment extends Fragment implements AdminDashboardContract.View {

    private AdminDashboardContract.Presenter mPresenter;
    private NavController navController;
    private MaterialCardView btnManageProducts, btnManageUsers, btnViewOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy NavController từ View
        setNavController();
        // Khởi tạo Presenter
        setupPresenter();
        // Khởi tạo các thành phần giao diện
        setupViews(view);
        // Thiết lập các sự kiện click cho các nút
        setupListeners();
        // Bắt đầu hoạt động của Presenter
        mPresenter.start();
    }
    private void setNavController() {
        navController = Navigation.findNavController(requireView());
    }

    private void setupPresenter() {
        mPresenter = new AdminDashboardPresenter(this);
    }
    private void setupViews(View view) {
        btnManageProducts = view.findViewById(R.id.btn_manage_products);
        btnManageUsers = view.findViewById(R.id.btn_manage_users);
        btnViewOrders = view.findViewById(R.id.btn_view_orders);
    }

    private void setupListeners() {
        btnManageProducts.setOnClickListener(v -> mPresenter.onManageProductsClicked());
        btnManageUsers.setOnClickListener(v -> mPresenter.onManageUsersClicked());
        btnViewOrders.setOnClickListener(v -> mPresenter.onViewOrdersClicked());

    }

    @Override
    public void setPresenter(AdminDashboardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void navigateToManageProducts() {
        navController.navigate(R.id.action_adminDashboardFragment_to_manageProductsFragment);
    }

    @Override
    public void navigateToManageUsers() {
        navController.navigate(R.id.action_adminDashboardFragment_to_manageUsersFragment);
    }

    @Override
    public void navigateToViewOrders() {
        navController.navigate(R.id.action_adminDashboardFragment_to_viewOrdersFragment);
    }
}
