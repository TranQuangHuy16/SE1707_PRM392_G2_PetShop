package com.example.se1707_prm392_g2_petshop.ui.user.main.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;

public class NotificationsFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.applySystemBarInsets(root);

        return root;
    }
}
