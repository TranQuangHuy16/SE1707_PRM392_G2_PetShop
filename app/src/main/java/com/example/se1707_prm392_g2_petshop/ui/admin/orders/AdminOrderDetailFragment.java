package com.example.se1707_prm392_g2_petshop.ui.admin.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.OrderDetailAdapter;
import com.example.se1707_prm392_g2_petshop.data.models.Order;
import com.example.se1707_prm392_g2_petshop.data.models.OrderDetail;
import com.example.se1707_prm392_g2_petshop.data.repositories.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AdminOrderDetailFragment extends Fragment {

    private static final String ARG_ORDER_ID = "order_id";

    private int orderId;
    private OrderRepository orderRepository;

    private TextView tvOrderId, tvOrderDate, tvStatus, tvTotalAmount, tvAddress;
    private RecyclerView rvOrderDetails;
    private OrderDetailAdapter adapter;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public static AdminOrderDetailFragment newInstance(int orderId) {
        AdminOrderDetailFragment fragment = new AdminOrderDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderId = getArguments() != null ? getArguments().getInt(ARG_ORDER_ID) : 0;
        orderRepository = OrderRepository.getInstance(requireContext());

        tvOrderId = view.findViewById(R.id.tvOrderId);
        tvOrderDate = view.findViewById(R.id.tvOrderDate);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        rvOrderDetails = view.findViewById(R.id.rvOrderDetails);

        rvOrderDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderDetailAdapter(requireContext());
        rvOrderDetails.setAdapter(adapter);

        // Click vào status để update
        tvStatus.setOnClickListener(v -> showStatusMenu(currentOrder));

        loadOrderDetail();
    }

    private Order currentOrder;

    private void loadOrderDetail() {
        orderRepository.getOrderById(orderId).observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                currentOrder = order;

                tvOrderId.setText("Đơn hàng #" + order.getOrderId());
                tvOrderDate.setText(dateFormat.format(order.getOrderDate()));
                tvStatus.setText(order.getStatusDisplay());
                tvTotalAmount.setText(String.format("%,.0fđ", order.getTotalAmount()));
                // Hiển thị chi tiết sản phẩm
                adapter.setOrderDetails(order.getOrderDetails());
            } else {
                Toast.makeText(getContext(), "Không thể tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStatusMenu(Order order) {
        if (order == null) return;

        String[] statuses = {"Pending", "Paid", "Cancelled", "Shipping", "Delivered"};

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Chọn trạng thái mới")
                .setItems(statuses, (dialog, which) -> {
                    String selectedStatus = statuses[which];

                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setTitle("Xác nhận")
                            .setMessage("Bạn có chắc chắn muốn cập nhật trạng thái thành " + selectedStatus + "?")
                            .setPositiveButton("Có", (d, w) -> {
                                updateOrderStatus(order, selectedStatus);
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                })
                .show();
    }

    private void updateOrderStatus(Order order, String status) {
        orderRepository.updateOrderStatus(order.getOrderId(), status)
                .observe(getViewLifecycleOwner(), success -> {
                    if (success != null && success) {
                        Toast.makeText(getContext(), "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                        loadOrderDetail(); // reload chi tiết
                    } else {
                        Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
