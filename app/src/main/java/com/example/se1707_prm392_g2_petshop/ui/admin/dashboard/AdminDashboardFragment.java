package com.example.se1707_prm392_g2_petshop.ui.admin.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.ProductRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.OrderRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.google.android.material.card.MaterialCardView;

public class AdminDashboardFragment extends Fragment implements AdminDashboardContract.View {
    private AdminDashboardContract.Presenter mPresenter;
    private NavController navController;
    private MaterialCardView btnManageProducts, btnManageUsers, btnViewOrders;
    private TextView tvTotalUsers, tvTotalProducts, tvTotalOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo Presenter
        setupPresenter();
        // Khởi tạo các thành phần giao diện
        setupViews(view);
        
        // ✅ Fix notch & navigation bar - Áp dụng cho ScrollView
        View scrollView = view.findViewById(R.id.scroll_view_dashboard);
        if (scrollView != null) {
            WindowInsetsUtil.applySystemBarInsets(scrollView);
        }
        
        // Thiết lập các sự kiện click cho các nút
        setupListeners();
        // Bắt đầu hoạt động của Presenter
        mPresenter.start();
    }

    private void setupPresenter() {
        UserApi userApi = RetrofitClient.getUserApi(requireContext());
        mPresenter = new AdminDashboardPresenter(
                this,
                UserRepository.getInstance(requireContext()),
                OrderRepository.getInstance(requireContext()),
                ProductRepository.getInstance(requireContext())
        );
    }
    private void setupViews(View view) {
        btnManageProducts = view.findViewById(R.id.btn_manage_products);
        btnManageUsers = view.findViewById(R.id.btn_manage_users);
        btnViewOrders = view.findViewById(R.id.btn_view_orders);
        tvTotalUsers = view.findViewById(R.id.tv_total_users);
        tvTotalProducts = view.findViewById(R.id.tv_total_products);
        tvTotalOrders = view.findViewById(R.id.tv_total_orders);
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
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_adminDashboardFragment_to_manageProductsFragment);
    }

    @Override
    public void navigateToManageUsers() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_adminDashboardFragment_to_manageUsersFragment);
    }

    @Override
    public void navigateToViewOrders() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_adminDashboardFragment_to_viewOrdersFragment);
    }

    @Override
    public void showTotalUsers(int count) {
        tvTotalUsers.setText(String.valueOf(count));
    }

    @Override
    public void showTotalProducts(int count) {
        tvTotalProducts.setText(String.valueOf(count));
    }

    @Override
    public void showTotalOrders(int count) {
        tvTotalOrders.setText(String.valueOf(count));
    }

    @Override
    public void showLoading() {
    // Nếu cần thêm logic hiển thị loading
        // thêm ProgressBar vào layout của Fragment
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
