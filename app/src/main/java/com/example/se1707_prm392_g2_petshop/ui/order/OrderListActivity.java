package com.example.se1707_prm392_g2_petshop.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.OrderAdapter;
import com.example.se1707_prm392_g2_petshop.data.models.Order;
import com.example.se1707_prm392_g2_petshop.data.repositories.OrderRepository;

import java.util.List;

public class OrderListActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {

    private RecyclerView rvOrders;
    private LinearLayout layoutEmpty;
    private ProgressBar progressBar;
    private ImageView btnBack;

    private OrderAdapter orderAdapter;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initViews();
        setupRecyclerView();
        loadOrders();
    }

    private void initViews() {
        rvOrders = findViewById(R.id.rvOrders);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);

        orderRepository = OrderRepository.getInstance(this);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(this, this);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);
        rvOrders.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);

        orderRepository.getMyOrders().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                progressBar.setVisibility(View.GONE);

    if (orders != null && !orders.isEmpty()) {
                    orderAdapter.setOrders(orders);
                    rvOrders.setVisibility(View.VISIBLE);
                    layoutEmpty.setVisibility(View.GONE);
                } else {
                    rvOrders.setVisibility(View.GONE);
                    layoutEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onOrderClick(Order order) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("orderId", order.getOrderId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
