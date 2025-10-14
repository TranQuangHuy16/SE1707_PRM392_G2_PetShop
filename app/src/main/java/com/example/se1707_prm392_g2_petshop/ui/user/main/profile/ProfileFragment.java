package com.example.se1707_prm392_g2_petshop.ui.user.main.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.databinding.ActivityLoginBinding;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentProfileBinding;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginPresenter;

public class ProfileFragment extends Fragment implements ProfileContract.View{

    private FragmentProfileBinding binding;
    private ProfilePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        setupPresenter();
        setupListeners();

        return binding.getRoot();
    }

    private void setupPresenter() {
        AuthApi authApi = RetrofitClient.getAuthApi(requireContext());
        AuthRepository authRepository = new AuthRepository(authApi);
        presenter = new ProfilePresenter(this, authRepository);
    }



    private void setupListeners() {
        binding.btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = requireContext().getSharedPreferences("auth_prefs", (int) Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.remove(getString(R.string.jwt_token_name));
//            editor.apply();
            JwtUtil.RemoveJwtTokenFromSharedPreferences(prefs);
            // Hiển thị thông báo và chuyển
            presenter.logout();
        });
    }

    @Override
    public void onLogoutSuccess(String message) {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLogoutFailure(String message) {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLogoutError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

    }
}
