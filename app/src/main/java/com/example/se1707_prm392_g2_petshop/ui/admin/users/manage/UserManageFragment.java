package com.example.se1707_prm392_g2_petshop.ui.admin.users.manage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;

public class UserManageFragment extends Fragment implements UserManageContract.View {

    private UserManageContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Return layout if exists, otherwise just a placeholder
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.applySystemBarInsets(view);
    }

    public void setPresenter(UserManageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // Các phương thức khác của UserManageFragment

}
