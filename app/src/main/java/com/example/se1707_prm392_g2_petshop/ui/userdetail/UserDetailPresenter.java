package com.example.se1707_prm392_g2_petshop.ui.userdetail;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateUserDetailsRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    // Tham chiếu đến View (Activity) và Repository
    private UserDetailContract.View view;
    private UserRepository userRepository;
    // LifecycleOwner (chính là Activity) để tự động quản lý LiveData
    private LifecycleOwner lifecycleOwner;

    // Constructor nhận các dependency từ Activity
    public UserDetailPresenter(UserDetailContract.View view, UserRepository userRepository, LifecycleOwner lifecycleOwner) {
        this.view = view;
        this.userRepository = userRepository;
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * Yêu cầu UserRepository tải chi tiết user
     * @param userId ID của user cần tải
     */
    @Override
    public void loadUserDetails(int userId) {
        if (view == null) return;
        view.showLoading(true);

        // Gọi repository và observe LiveData
        userRepository.getUserDetail(userId).observe(lifecycleOwner, new Observer<UserDetailResponse>() {
            @Override
            public void onChanged(UserDetailResponse userDetailResponse) {
                if (view == null) return; // View có thể đã bị hủy
                view.showLoading(false);

                if (userDetailResponse != null) {
                    view.showUserDetails(userDetailResponse);
                } else {
                    view.showError("Failed to load user details.");
                }
            }
        });
    }

    /**
     * Yêu cầu UserRepository cập nhật chi tiết user
     * @param userId ID của user cần cập nhật
     * @param request Dữ liệu mới
     */
    @Override
    public void updateUserDetails(int userId, UpdateUserDetailsRequest request) {
        if (view == null) return;
        view.showLoading(true); // Hiển thị loading cho cả quá trình (upload + update)

        // Gọi repository và observe LiveData
        userRepository.updateUserDetails(userId, request).observe(lifecycleOwner, new Observer<UserDetailResponse>() {
            @Override
            public void onChanged(UserDetailResponse userDetailResponse) {
                if (view == null) return;
                view.showLoading(false);

                if (userDetailResponse != null) {
                    view.showUpdateSuccess("User details updated successfully!");
                } else {
                    view.showError("Failed to update user details.");
                }
            }
        });
    }

    /**
     * Hủy tham chiếu đến View khi Activity bị destroy
     * để tránh memory leak
     */
    @Override
    public void onDestroy() {
        view = null;
    }
}