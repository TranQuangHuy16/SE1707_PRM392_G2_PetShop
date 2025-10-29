package com.example.se1707_prm392_g2_petshop.ui.admin.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.se1707_prm392_g2_petshop.R;

public class AdminProductsFragment extends Fragment implements AdminProductsContract.View {

    private AdminProductsContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new AdminProductsPresenter(this);
        mPresenter.start();
    }

    @Override
    public void setPresenter(AdminProductsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
