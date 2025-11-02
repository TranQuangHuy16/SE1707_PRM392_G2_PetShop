package com.example.se1707_prm392_g2_petshop.ui.admin.users.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.UserDetailResponse;
import com.example.se1707_prm392_g2_petshop.data.models.User; // Changed to User model
import com.example.se1707_prm392_g2_petshop.data.models.ModelEnums.UserRoleEnum;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.databinding.FragmentAdminUsersBinding;
import com.example.se1707_prm392_g2_petshop.ui.admin.users.adapters.AdminUsersAdapter;

import java.util.List;

public class AdminUsersFragment extends Fragment implements AdminUsersContract.View {

    private AdminUsersContract.Presenter mPresenter;
    private FragmentAdminUsersBinding binding;
    private AdminUsersAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPresenter();
        setupUI();
        
        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.applySystemBarInsets(view);
        
        mPresenter.loadUsers();
    }

    private void setupUI() {
        mAdapter = new AdminUsersAdapter(mPresenter);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvUsers.setAdapter(mAdapter);

        setupFilterDropdowns();
    }

    private void setupFilterDropdowns() {
        // Setup roles dropdown
        UserRoleEnum[] enumValues = UserRoleEnum.values();
        String[] roles = new String[enumValues.length + 1];
        roles[0] = "All Roles";
        for (int i = 0; i < enumValues.length; i++) {
            roles[i + 1] = enumValues[i].getValue(); // Sử dụng getValue() để lấy "Admin", "Customer"
        }
        ArrayAdapter<String> roleAdapter
                = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, roles);
        binding.dropdownFilterRole.setAdapter(roleAdapter);
        binding.dropdownFilterRole.setText(roles[0], false); // Set default

        // Setup status dropdown
        String[] statuses = {"All Status", "Active", "Inactive"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, statuses);
        binding.dropdownFilterStatus.setAdapter(statusAdapter);
        binding.dropdownFilterStatus.setText(statuses[0], false); // Set default

        // Gọi presenter khi filter thay đổi
        binding.dropdownFilterRole.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRole = parent.getItemAtPosition(position).toString();
            String selectedStatus = binding.dropdownFilterStatus.getText().toString();
            mPresenter.handleFilterChanged(selectedRole, selectedStatus);
        });

        binding.dropdownFilterStatus.setOnItemClickListener((parent, view, position, id) -> {
            String selectedStatus = parent.getItemAtPosition(position).toString();
            String selectedRole = binding.dropdownFilterRole.getText().toString();
            mPresenter.handleFilterChanged(selectedRole, selectedStatus);
        });
    }

    @Override
    public void displayUsers(List<UserDetailResponse> users) {
        mAdapter.submitList(users);
    }


    public void setPresenter() {
        UserRepository userRepository = UserRepository.getInstance(requireContext());
        UserAddressRepository userAddressRepository = UserAddressRepository.getInstance(requireContext());
        mPresenter = new AdminUsersPresenter(this, userRepository, userAddressRepository);
    }

    @Override
    public void showLoading() {
        if (binding != null) {
            binding.progressLoadingUsers.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (binding != null) {
            binding.progressLoadingUsers.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToManageScreen(int userId) {
//        AdminUsersFragmentDirections.ActionAdminUsersFragmentToUserManageFragment action =
//                AdminUsersFragmentDirections.actionAdminUsersFragmentToUserManageFragment(userId);
//        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
