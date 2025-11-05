package com.example.se1707_prm392_g2_petshop.ui.admin.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;

public class AdminSettingsFragment extends Fragment implements AdminSettingsContract.View {

    private AdminSettingsContract.Presenter mPresenter;
    
    // Views
    private ImageView imgAdminAvatar;
    private TextView tvAdminFullname;
    private TextView tvAdminUsername;
    private TextView tvAdminEmail;
    private TextView tvAdminPhone;
    private TextView tvAdminRole;
    private TextView tvAdminStatus;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupPresenter();
        setupViews(view);
        setupListeners();
        
        // ✅ Fix notch & navigation bar - Áp dụng cho ScrollView
        View scrollView = view.findViewById(R.id.scroll_view_settings);
        if (scrollView != null) {
            WindowInsetsUtil.applySystemBarInsets(scrollView);
        }
        
        // Load thông tin Admin
        mPresenter.loadAdminInfo();
    }

    private void setupPresenter() {
        AuthApi authApi = RetrofitClient.getAuthApi(requireContext());
        AuthRepository authRepository = new AuthRepository(authApi);
        UserRepository userRepository = UserRepository.getInstance(requireContext());
        mPresenter = new AdminSettingsPresenter(this, authRepository, userRepository, requireContext());
    }

    private void setupViews(View view) {
        imgAdminAvatar = view.findViewById(R.id.img_admin_avatar);
        tvAdminFullname = view.findViewById(R.id.tv_admin_fullname);
        tvAdminUsername = view.findViewById(R.id.tv_admin_username);
        tvAdminEmail = view.findViewById(R.id.tv_admin_email);
        tvAdminPhone = view.findViewById(R.id.tv_admin_phone);
        tvAdminRole = view.findViewById(R.id.tv_admin_role);
        tvAdminStatus = view.findViewById(R.id.tv_admin_status);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> mPresenter.logout());
    }

    @Override
    public void displayAdminInfo(UserDetailResponse adminInfo) {
        if (adminInfo == null) return;

        // Hiển thị avatar
        if (adminInfo.getImgUrl() != null && !adminInfo.getImgUrl().isEmpty()) {
            Glide.with(requireContext())
                    .load(adminInfo.getImgUrl())
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .circleCrop()
                    .into(imgAdminAvatar);
        }

        // Hiển thị thông tin cơ bản
        tvAdminFullname.setText(adminInfo.getFullName() != null ? adminInfo.getFullName() : "N/A");
        tvAdminUsername.setText("@" + (adminInfo.getUsername() != null ? adminInfo.getUsername() : "N/A"));
        tvAdminEmail.setText(adminInfo.getEmail() != null ? adminInfo.getEmail() : "N/A");
        tvAdminPhone.setText(adminInfo.getPhone() != null && !adminInfo.getPhone().isEmpty() ? adminInfo.getPhone() : "N/A");
        tvAdminRole.setText(adminInfo.getRole() != null ? adminInfo.getRole() : "Admin");
        
        // Hiển thị status với màu sắc
        if (adminInfo.isActive()) {
            tvAdminStatus.setText("Active");
            tvAdminStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvAdminStatus.setText("Inactive");
            tvAdminStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public void showLoadingError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(AdminSettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onLogoutSuccess(String message) {
        // 1. Xóa token khỏi SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        JwtUtil.RemoveJwtTokenFromSharedPreferences(prefs);

        // 2. Hiển thị thông báo
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

        // 3. Chuyển về màn hình Login
        navigateToLogin();
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onLogoutFailure(String message) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onLogoutError(String message) {
        Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
