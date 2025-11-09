package com.example.se1707_prm392_g2_petshop.ui.user.main.profile;

import android.app.Activity;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentProfileBinding;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;
import com.example.se1707_prm392_g2_petshop.ui.chat.ChatActivity;
import com.example.se1707_prm392_g2_petshop.ui.userdetail.UserDetailActivity;

public class ProfileFragment extends Fragment implements ProfileContract.View {

    private FragmentProfileBinding binding;
    private ProfilePresenter presenter;
    private TextView tvUserName, tvEmail;
    private ImageView imgAvatar;

    // 1. Thêm ActivityResultLauncher
    private ActivityResultLauncher<Intent> userDetailLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        initView();
        setupPresenter();
        setupMenuItems();
        setupAddressItems();

        // 2. Đăng ký launcher TRƯỚC khi setup listeners
        registerUserDetailLauncher();

        setupListeners(); // 3. Gọi setupListeners (sau khi đăng ký)

        // Fix notch & navigation bar - Áp dụng cho ScrollView
        View scrollView = binding.getRoot().findViewById(R.id.scroll_view_profile);
        if (scrollView != null) {
            WindowInsetsUtil.applySystemBarInsets(scrollView);
        }

        // Tải thông tin user
        String id = JwtUtil.getSubFromToken(requireContext());
        int userId = id != null ? Integer.parseInt(id) : -1;
        presenter.getUserProfile(userId);

        return binding.getRoot();
    }

    // 4. Thêm hàm mới để đăng ký launcher
    private void registerUserDetailLauncher() {
        userDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Hàm này sẽ được gọi khi UserDetailActivity đóng lại
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Nếu kết quả là OK (do UserDetailActivity set)
                        Log.d("ProfileFragment", "Returned from UserDetailActivity with RESULT_OK. Reloading profile...");

                        // Tải lại thông tin profile
                        String id = JwtUtil.getSubFromToken(requireContext());
                        int userId = id != null ? Integer.parseInt(id) : -1;
                        if (userId != -1) {
                            presenter.getUserProfile(userId);
                        }
                    } else {
                        // Nếu người dùng chỉ bấm Back (RESULT_CANCELLED)
                        Log.d("ProfileFragment", "Returned from UserDetailActivity without changes.");
                    }
                }
        );
    }

    private void initView() {
        tvUserName = binding.tvUserName;
        tvEmail = binding.tvEmail;
        imgAvatar = binding.imgAvatar;
    }

    private void setupMenuItems() {
        binding.itemOrder.tvTitle.setText("My Orders");
        binding.itemOrder.imgIcon.setImageResource(R.drawable.ic_order);
        binding.itemProfile.tvTitle.setText("My Profile");
        binding.itemProfile.imgIcon.setImageResource(R.drawable.ic_profile);
        binding.itemChat.tvTitle.setText("My Chats");
        binding.itemChat.imgIcon.setImageResource(R.drawable.ic_chat);
        binding.itemContact.tvTitle.setText("Contact Us");
        binding.itemContact.imgIcon.setImageResource(R.drawable.ic_contact);
        binding.itemHelp.tvTitle.setText("Help & FAQs");
        binding.itemHelp.imgIcon.setImageResource(R.drawable.ic_help);
    }

    private void setupAddressItems() {
        binding.itemAddress.tvTitle.setText("My Address");
        binding.itemAddress.imgIcon.setImageResource(R.drawable.ic_address);
    }

    private void setupPresenter() {
        AuthApi authApi = RetrofitClient.getAuthApi(requireContext());
        UserApi userApi = RetrofitClient.getUserApi(requireContext());
        AuthRepository authRepository = new AuthRepository(authApi);
        UserRepository userRepository = new UserRepository(requireContext());
        presenter = new ProfilePresenter(this, authRepository, userRepository);
    }


    private void setupListeners() {
        binding.itemProfile.getRoot().setOnClickListener(v -> {
            Log.d("ProfileFragment", "My Profile clicked!");
            try {
                Intent intent = new Intent(requireContext(),
                        UserDetailActivity.class);

                // 5. Dùng LAUNCHER thay vì startActivity
                userDetailLauncher.launch(intent); // <-- THAY BẰNG DÒNG NÀY

                Log.d("ProfileFragment", "UserDetailActivity started for result");
            } catch (Exception e) {
                Log.e("ProfileFragment", "Error starting UserDetailActivity", e);
                Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

        binding.itemAddress.getRoot().setOnClickListener(v -> {
            ;
            Log.d("ProfileFragment", "My Address clicked!");
            try {
                Intent intent = new Intent(requireContext(),
                        com.example.se1707_prm392_g2_petshop.ui.address.list.AddressListActivity.class);
                startActivity(intent);
                Log.d("ProfileFragment", "UserAddressListActivity started");
            } catch (Exception e) {
                Log.e("ProfileFragment", "Error starting UserAddressListActivity", e);
                Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.itemChat.getRoot().setOnClickListener(v -> {
            try {
                Intent intent = new Intent(requireContext(),
                        ChatActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = requireContext().getSharedPreferences("auth_prefs", (int) Context.MODE_PRIVATE);
            JwtUtil.RemoveJwtTokenFromSharedPreferences(prefs);
            // Hiển thị thông báo và chuyển
            presenter.logout();
        });
    }

    @Override
    public void onLogoutSuccess(String message) {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onLogoutFailure(String message) {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        // ✅ SỬ DỤNG getImgAvatarl() THEO YÊU CẦU CỦA BẠN
        Glide.with(this)
                .load(user.getImgAvatarl()) // <-- Đã dùng tên hàm bạn xác nhận
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .circleCrop()
                .into(imgAvatar);
    }

    @Override
    public void onGetUserProfileFailure(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetUserProfileError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}