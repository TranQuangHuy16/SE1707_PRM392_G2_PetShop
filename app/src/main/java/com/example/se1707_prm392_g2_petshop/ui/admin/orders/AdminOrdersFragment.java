package com.example.se1707_prm392_g2_petshop.ui.admin.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.se1707_prm392_g2_petshop.R;

public class AdminOrdersFragment extends Fragment implements AdminOrdersContract.View {

    private AdminOrdersContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AdminOrdersPresenter(this);
        mPresenter.start();
    }

    @Override
    public void setPresenter(AdminOrdersContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
