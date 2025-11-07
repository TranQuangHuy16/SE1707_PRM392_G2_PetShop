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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.AdminOrdersAdapter;
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
            adapter = new AdminOrdersAdapter(orderList, new AdminOrdersAdapter.OnOrderClickListener() {
            @Override
            public void onOrderClick(Order order) {
                Bundle bundle = new Bundle();
                bundle.putInt("order_id", order.getOrderId());
                NavHostFragment.findNavController(AdminOrdersFragment.this)
                        .navigate(R.id.action_adminOrdersFragment_to_adminOrderDetailFragment, bundle);
            }

            @Override
            public void onStatusClick(Order order) {
                // Khi click vào TextView trạng thái
                showStatusMenu(order);  // gọi method hiển thị menu chọn status
            }
        });
            rvOrders.setAdapter(adapter);
            OrderRepository orderRepository = OrderRepository.getInstance(requireContext());

            // 👉 Khởi tạo Presenter (truyền repository thật)
            mPresenter = new AdminOrdersPresenter(this, orderRepository);
            mPresenter.start();
    }

    // --------------------------------------------------------------------------------------------

    private void showStatusMenu(Order order) {
        // Các trạng thái có thể cập nhật
        String[] statuses = {"Pending", "Paid", "Cancelled", "Shipping", "Delivered"};

        // Dialog chọn trạng thái
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn trạng thái mới");
        builder.setItems(statuses, (dialog, which) -> {
            String selectedStatus = statuses[which];

            // Dialog xác nhận trước khi update
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn cập nhật trạng thái thành " + selectedStatus + "?")
                    .setPositiveButton("Có", (d, w) -> {
                        // Gọi presenter để thực hiện update
                        mPresenter.onUpdateStatusClicked(order, selectedStatus);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        builder.show();
    }


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
