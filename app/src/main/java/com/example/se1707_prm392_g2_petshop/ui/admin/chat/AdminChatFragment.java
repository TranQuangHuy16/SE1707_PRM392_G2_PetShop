package com.example.se1707_prm392_g2_petshop.ui.admin.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;

public class AdminChatFragment extends Fragment implements AdminChatContract.View {

    private AdminChatContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.applySystemBarInsets(view);

        new AdminChatPresenter(this);
        mPresenter.start();
    }

    @Override
    public void setPresenter(AdminChatContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
