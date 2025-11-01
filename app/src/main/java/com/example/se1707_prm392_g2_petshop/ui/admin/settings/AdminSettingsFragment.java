package com.example.se1707_prm392_g2_petshop.ui.admin.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;

public class AdminSettingsFragment extends Fragment implements AdminSettingsContract.View {

    private AdminSettingsContract.Presenter mPresenter;
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
    }

    private void setupPresenter() {
        AuthApi authApi = RetrofitClient.getAuthApi(requireContext());
        AuthRepository authRepository = new AuthRepository(authApi);
        mPresenter = new AdminSettingsPresenter(this, authRepository);
    }

    private void setupViews(View view) {
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> mPresenter.logout());
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onLogoutFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogoutError(String message) {
        Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
