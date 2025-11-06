package com.example.se1707_prm392_g2_petshop.ui.admin.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.models.Order;
import com.example.se1707_prm392_g2_petshop.data.repositories.OrderRepository;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersFragment extends Fragment implements AdminOrdersContract.View {

    private AdminOrdersContract.Presenter mPresenter;
    private RecyclerView rvOrders;
    private ProgressBar progressBar;
    private AdminOrdersAdapter adapter;
    private final List<Order> orderList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // ✅ Căn notch & navigation bar
            WindowInsetsUtil.applySystemBarInsets(view);

            rvOrders = view.findViewById(R.id.recycler_orders);
            progressBar = view.findViewById(R.id.progress_loading_orders);

            rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new AdminOrdersAdapter(orderList, order -> {
                mPresenter.onOrderClicked(order);
            });
            rvOrders.setAdapter(adapter);
            OrderRepository orderRepository = OrderRepository.getInstance(requireContext());

            // 👉 Khởi tạo Presenter (truyền repository thật)
            mPresenter = new AdminOrdersPresenter(this, orderRepository);
            mPresenter.start();
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public void setPresenter(AdminOrdersContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showOrders(List<Order> orders) {
        orderList.clear();
        orderList.addAll(orders);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
