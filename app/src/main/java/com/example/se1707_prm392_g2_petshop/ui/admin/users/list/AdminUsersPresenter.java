package com.example.se1707_prm392_g2_petshop.ui.admin.users.list;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UpdateUserRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.models.ModelEnums;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersPresenter implements AdminUsersContract.Presenter {

    private final AdminUsersContract.View mView;

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    // Lưu trữ danh sách đầy đủ và danh sách hiển thị
    private List<UserDetailResponse> allUsers = new ArrayList<>();
    private String currentFilterRole = "All Roles";
    private String currentFilterStatus = "All Status";


    public AdminUsersPresenter(
            AdminUsersContract.View mView,
            UserRepository userRepository,
            UserAddressRepository userAddressRepository
    ) {
        this.mView = mView;
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public void start() {
        if (mView == null) return;
        mView.showLoading();
        loadUsers();
    }

    @Override
    public void loadUsers() {
        if (mView == null) return;
        mView.showLoading();

        userRepository.getAllUser().observe((LifecycleOwner) mView, userList -> {
            Log.d("AdminUsersPresenter",
                    "Users loaded: " + (userList != null ? userList.size() : 0));

            userAddressRepository.getUserAddresses().observe((LifecycleOwner) mView, allAddresses -> {

                if (userList != null && !userList.isEmpty() && allAddresses != null && mView != null) {

                    List<UserDetailResponse> userDetailResponseList = new ArrayList<>();

                    // 2. Duyệt qua từng người dùng để tạo UserDetailResponse
                    for (User user : userList) {
                        // 3. Lọc danh sách địa chỉ cho người dùng hiện tại
                        List<UserAddress> userSpecificAddresses = new ArrayList<>();
                        for(UserAddress address : allAddresses) {
                            if (address.getUserId() == user.getUserId()) {
                                userSpecificAddresses.add(address);
                            }
                        }
                        // 4. Tạo đối tượng UserDetailResponse với danh sách địa chỉ tương ứng
                        UserDetailResponse detailResponse = new UserDetailResponse(
                                user.getUserId(),
                                user.getUsername(),
                                user.getFullName(),
                                user.getEmail(),
                                user.getPhone(),
                                user.getImgAvatarl(),
                                ModelEnums.UserRoleEnum.fromInt(user.getRole()).getValue(),
                                user.isActive(),
                                user.getCreatedAt(),
                                userSpecificAddresses // Thêm danh sách địa chỉ vào constructor
                        );
                        userDetailResponseList.add(detailResponse);
                    }

                    // 5. Lưu danh sách đầy đủ và áp dụng filter
                    allUsers = userDetailResponseList;
                    applyFilters();

                } else if (mView != null) {
                    mView.showErrorMessage("Failed to load users or addresses.");
                }

                // 6. Ẩn loading sau khi đã xử lý xong
                if (mView != null) {
                    mView.hideLoading();
                }

                // Gỡ bỏ observer để tránh gọi lại không cần thiết
                userAddressRepository.getUserAddresses().removeObservers((LifecycleOwner) mView);
            });
            // Gỡ bỏ observer để tránh gọi lại không cần thiết
            userRepository.getAllUser().removeObservers((LifecycleOwner) mView);
        });
    }

    @Override
    public void handleFilterChanged(String selectedRole, String selectedStatus) {
        currentFilterRole = selectedRole;
        currentFilterStatus = selectedStatus;
        applyFilters();
    }

    /**
     * Áp dụng filter dựa trên role và status được chọn
     */
    private void applyFilters() {
        if (allUsers == null || allUsers.isEmpty()) {
            if (mView != null) {
                mView.displayUsers(new ArrayList<>());
            }
            return;
        }

        List<UserDetailResponse> filteredList = new ArrayList<>();

        for (UserDetailResponse user : allUsers) {
            boolean matchRole = isRoleMatch(user, currentFilterRole);
            boolean matchStatus = isStatusMatch(user, currentFilterStatus);

            if (matchRole && matchStatus) {
                filteredList.add(user);
            }
        }

        if (mView != null) {
            mView.displayUsers(filteredList);
            Log.d("AdminUsersPresenter", 
                "Filtered: " + filteredList.size() + " / " + allUsers.size() + 
                " (Role: " + currentFilterRole + ", Status: " + currentFilterStatus + ")");
        }
    }

    /**
     * Kiểm tra user có khớp với role filter không
     */
    private boolean isRoleMatch(UserDetailResponse user, String filterRole) {
        if (filterRole == null || filterRole.equals("All Roles")) {
            return true;
        }
        return user.getRole() != null && user.getRole().equalsIgnoreCase(filterRole);
    }

    /**
     * Kiểm tra user có khớp với status filter không
     */
    private boolean isStatusMatch(UserDetailResponse user, String filterStatus) {
        if (filterStatus == null || filterStatus.equals("All Status")) {
            return true;
        }
        
        boolean isActive = user.isActive();
        
        if (filterStatus.equals("Active")) {
            return isActive;
        } else if (filterStatus.equals("Inactive")) {
            return !isActive;
        }
        
        return true;
    }

    // Click để xem chi tiết user
    @Override
    public void handleUserClicked(int userId){

    }

    // Đổi trạng thái user
    @Override
    public void handleActiveToggled(UserDetailResponse user, boolean isActive) {
        if (user == null) return;

        // Chuyển đổi role string sang int
        int roleInt = ModelEnums.UserRoleEnum.fromString(user.getRole()).ordinal();

        // Tạo request để update
        UpdateUserRequest request = new UpdateUserRequest(roleInt, isActive);

        // Gọi API update
        userRepository.updateUser(user.getUserId(), request).observe((LifecycleOwner) mView, updatedUser -> {
            if (updatedUser != null) {
                // Update thành công
                if (mView != null) {
                    mView.showSuccessMessage("User status updated successfully!");
                }

                // Cập nhật trong danh sách local
                updateUserInList(user.getUserId(), isActive, user.getRole());
                applyFilters(); // Áp dụng lại filter để refresh UI

            } else {
                // Update thất bại
                if (mView != null) {
                    mView.showErrorMessage("Failed to update user status.");
                }
                // Reload lại danh sách để đảm bảo dữ liệu chính xác
                loadUsers();
            }

            // Gỡ bỏ observer
            userRepository.updateUser(user.getUserId(), request).removeObservers((LifecycleOwner) mView);
        });
    }

    // Đổi role
    @Override
    public void handleRoleChanged(UserDetailResponse user, String newRole) {
        if (user == null) return;

        // Chuyển đổi role string sang int
        int roleInt = ModelEnums.UserRoleEnum.fromString(newRole).ordinal();

        // Tạo request để update (giữ nguyên isActive)
        UpdateUserRequest request = new UpdateUserRequest(roleInt, user.isActive());

        // Gọi API update
        userRepository.updateUser(user.getUserId(), request).observe((LifecycleOwner) mView, updatedUser -> {
            if (updatedUser != null) {
                // Update thành công
                if (mView != null) {
                    mView.showSuccessMessage("User role updated successfully!");
                }

                // Cập nhật trong danh sách local
                updateUserInList(user.getUserId(), user.isActive(), newRole);
                applyFilters(); // Áp dụng lại filter để refresh UI

            } else {
                // Update thất bại
                if (mView != null) {
                    mView.showErrorMessage("Failed to update user role.");
                }
                // Reload lại danh sách để đảm bảo dữ liệu chính xác
                loadUsers();
            }

            // Gỡ bỏ observer
            userRepository.updateUser(user.getUserId(), request).removeObservers((LifecycleOwner) mView);
        });
    }

    /**
     * Cập nhật thông tin user trong danh sách local (không cần gọi API lại)
     */
    private void updateUserInList(int userId, boolean isActive, String role) {
        if (allUsers == null || allUsers.isEmpty()) return;

        for (UserDetailResponse user : allUsers) {
            if (user.getUserId() == userId) {
                user.setActive(isActive);
                user.setRole(role);
                break;
            }
        }
    }
}
