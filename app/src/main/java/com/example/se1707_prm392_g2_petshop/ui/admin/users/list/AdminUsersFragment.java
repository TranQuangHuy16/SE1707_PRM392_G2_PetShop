package com.example.se1707_prm392_g2_petshop.ui.admin.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.se1707_prm392_g2_petshop.R;

public class AdminUsersFragment extends Fragment implements AdminUsersContract.View {

    private AdminUsersContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AdminUsersPresenter(this);
        mPresenter.start();
    }

    @Override
    public void setPresenter(AdminUsersContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
