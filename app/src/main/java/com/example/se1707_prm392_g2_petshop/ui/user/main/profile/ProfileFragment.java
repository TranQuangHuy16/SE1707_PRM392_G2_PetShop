package com.example.se1707_prm392_g2_petshop.ui.user.main.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.AuthRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.databinding.ActivityLoginBinding;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentProfileBinding;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginPresenter;

public class ProfileFragment extends Fragment implements ProfileContract.View{

    private FragmentProfileBinding binding;
    private ProfilePresenter presenter;
    private TextView tvUserName, tvEmail;
    private ImageView imgAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        initView();
        setupPresenter();
        setupMenuItems();
        setupListeners();

        String id = JwtUtil.getSubFromToken(requireContext());
        int userId = id != null ? Integer.parseInt(id) : -1;
        presenter.getUserProfile(userId);
        return binding.getRoot();
    }

    private void initView() {
        tvUserName = binding.tvUserName;
        tvEmail = binding.tvEmail;
        imgAvatar = binding.imgAvatar;
    }

    private void setupMenuItems() {
        binding.itemOrder.tvTitle.setText("My Orders");
        binding.itemOrder.imgIcon.setImageResource(R.drawable.ic_order);
    }

    private void setupPresenter() {
        AuthApi authApi = RetrofitClient.getAuthApi(requireContext());
        UserApi userApi = RetrofitClient.getUserApi(requireContext());
        AuthRepository authRepository = new AuthRepository(authApi);
        UserRepository userRepository = new UserRepository(requireContext());
        presenter = new ProfilePresenter(this, authRepository, userRepository);
    }



    private void setupListeners() {
        binding.itemOrder.getRoot().setOnClickListener(v -> {
            Log.d("ProfileFragment", "My Orders clicked!");
            try {
                Intent intent = new Intent(requireContext(), 
                    com.example.se1707_prm392_g2_petshop.ui.order.OrderListActivity.class);
                startActivity(intent);
                Log.d("ProfileFragment", "OrderListActivity started");
            } catch (Exception e) {
                Log.e("ProfileFragment", "Error starting OrderListActivity", e);
                Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onGetUserProfileSuccess(User user) {
        tvUserName.setText(user.getFullName());
        tvEmail.setText(user.getEmail());
        Glide.with(this)
                .load(user.getImgAvatarl())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .circleCrop()
                .into(imgAvatar);    }

    @Override
    public void onGetUserProfileFailure(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGetUserProfileError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

    }
}
