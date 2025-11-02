package com.example.se1707_prm392_g2_petshop.ui.admin.users.list;

import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;

import java.util.List;

public interface AdminUsersContract {
    interface View {
        void displayUsers(List<UserDetailResponse> users);
        void showLoading();
        void hideLoading();
        void showErrorMessage(String message);
        void showSuccessMessage(String message);
        void navigateToManageScreen(int userId);
    }

    interface Presenter {
        void start();
        void loadUsers();
        void handleFilterChanged(String selectedRole, String selectedStatus);
        void handleUserClicked(int userId);
        void handleActiveToggled(UserDetailResponse user, boolean isActive);
        void handleRoleChanged(UserDetailResponse user, String newRole);
    }
}
