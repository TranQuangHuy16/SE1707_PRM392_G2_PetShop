package com.example.se1707_prm392_g2_petshop.ui.order;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.dto.ZaloPayCreateRequest;
import com.example.petshop.dto.ZaloPayCreateResponse;
import com.example.petshop.dto.ZaloPayQueryResponse;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.OrderDetailAdapter;
import com.example.se1707_prm392_g2_petshop.data.api.PaymentApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.ConfirmPaymentResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Order;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.repositories.OrderRepository;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderDate, tvStatus, tvAddressInfo, tvTotalAmount;
    private RecyclerView rvOrderDetails;
    private CardView cardAddress;
    private AppCompatButton btnCancelOrder, btnPayment;
    private ProgressBar progressBar;
    private ImageView btnBack;

    private OrderDetailAdapter orderDetailAdapter;
    private OrderRepository orderRepository;
    private PaymentApi paymentApi;
    private int orderId;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderId = getIntent().getIntExtra("orderId", -1);
        
        handleDeepLink(getIntent());
        
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        loadOrderDetail();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleDeepLink(intent);
    }

    private void handleDeepLink(Intent intent) {
        Uri data = intent.getData();
        if (data != null && "petshop".equals(data.getScheme())) {
            // User quay về từ ZaloPay
            String path = data.getPath();
            if ("/callback".equals(path)) {
                // Get orderId from SharedPreferences if not set
                if (orderId <= 0) {
                    orderId = getSharedPreferences("payment_prefs", MODE_PRIVATE)
                        .getInt("pending_order_id", 0);
                }
                
                // Query payment status từ ZaloPay
                if (orderId > 0) {
                    // Clear pending order
                    getSharedPreferences("payment_prefs", MODE_PRIVATE)
                        .edit()
                        .remove("pending_order_id")
                        .apply();
                    
                    queryPaymentStatus(orderId);
                }
            }
        }
    }

    private void queryPaymentStatus(int orderId) {
        progressBar.setVisibility(View.VISIBLE);
        
        paymentApi.queryZaloPayPayment(orderId).enqueue(new Callback<ZaloPayQueryResponse>() {
            @Override
            public void onResponse(Call<ZaloPayQueryResponse> call, 
                                   Response<ZaloPayQueryResponse> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ZaloPayQueryResponse result = response.body();

                    if (result.getReturnCode() == 1) {
                        Toast.makeText(OrderDetailActivity.this, 
                            "Thanh toán thành công!", 
                            Toast.LENGTH_LONG).show();
                        
                        loadOrderDetail();
                    } else {
                        String message = "Chưa hoàn thành thanh toán";
                        if (result.getReturnMessage() != null && !result.getReturnMessage().isEmpty()) {
                            message = result.getReturnMessage();
                        }
                        Toast.makeText(OrderDetailActivity.this, 
                            message, 
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, 
                        "Không thể kiểm tra trạng thái thanh toán", 
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ZaloPayQueryResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderDetailActivity.this, 
                    "Lỗi kết nối: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmPayment(int orderId) {
        progressBar.setVisibility(View.VISIBLE);
        
        paymentApi.confirmPayment(orderId).enqueue(new Callback<ConfirmPaymentResponse>() {
            @Override
            public void onResponse(Call<ConfirmPaymentResponse> call, 
                                   Response<ConfirmPaymentResponse> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ConfirmPaymentResponse result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText(OrderDetailActivity.this, 
                            "Thanh toán thành công!", 
                            Toast.LENGTH_LONG).show();
                        
                        loadOrderDetail();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, 
                            result.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, 
                        "Không thể xác nhận thanh toán", 
                        Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConfirmPaymentResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderDetailActivity.this, 
                    "Lỗi: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvStatus = findViewById(R.id.tvStatus);
        tvAddressInfo = findViewById(R.id.tvAddressInfo);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvOrderDetails = findViewById(R.id.rvOrderDetails);
        cardAddress = findViewById(R.id.cardAddress);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnPayment = findViewById(R.id.btnPayment);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);

        orderRepository = OrderRepository.getInstance(this);
        paymentApi = RetrofitClient.getInstance(this).create(PaymentApi.class);

        btnBack.setOnClickListener(v -> finish());
        btnCancelOrder.setOnClickListener(v -> showCancelOrderDialog());
        btnPayment.setOnClickListener(v -> createZaloPayPayment());
    }

    private void setupRecyclerView() {
        orderDetailAdapter = new OrderDetailAdapter(this);
        rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        rvOrderDetails.setAdapter(orderDetailAdapter);
    }

    private void loadOrderDetail() {
        progressBar.setVisibility(View.VISIBLE);

        orderRepository.getOrderById(orderId).observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                progressBar.setVisibility(View.GONE);

                if (order != null) {
                    currentOrder = order;
                    displayOrderInfo(order);
                } else {
                    Toast.makeText(OrderDetailActivity.this, 
                        "Không thể tải thông tin đơn hàng", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void displayOrderInfo(Order order) {
        tvOrderId.setText("Đơn hàng #" + order.getOrderId());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvOrderDate.setText(sdf.format(order.getOrderDate()));
        
        tvStatus.setText(order.getStatusDisplay());
        
        // Set status color and button visibility
        int statusColor;
        switch (order.getStatus()) {
            case "Pending":
                statusColor = getColor(R.color.status_pending);
                btnCancelOrder.setVisibility(View.VISIBLE);
                btnPayment.setVisibility(View.VISIBLE);
                break;
            case "Paid":
            case "Processing":
                statusColor = getColor(R.color.status_paid);
                btnCancelOrder.setVisibility(View.GONE);
                btnPayment.setVisibility(View.GONE);
                break;
            case "Cancelled":
                statusColor = getColor(R.color.status_cancelled);
                btnCancelOrder.setVisibility(View.GONE);
                btnPayment.setVisibility(View.GONE);
                break;
            default:
                statusColor = getColor(R.color.black);
                btnCancelOrder.setVisibility(View.GONE);
                btnPayment.setVisibility(View.GONE);
        }
        tvStatus.setTextColor(statusColor);

        // Display address
        if (order.getAddress() != null) {
            String addressText = order.getAddress().getAddressLine() + ", " +
                    order.getAddress().getWard() + ", " +
                    order.getAddress().getDistrict() + ", " +
                    order.getAddress().getCity();
            tvAddressInfo.setText(addressText);
            cardAddress.setVisibility(View.VISIBLE);
        } else {
            cardAddress.setVisibility(View.GONE);
        }

        // Display order details
        orderDetailAdapter.setOrderDetails(order.getOrderDetails());

        // Display total amount
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalAmount.setText(formatter.format(order.getTotalAmount()));
    }

    private void showCancelOrderDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hủy đơn hàng")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> cancelOrder())
                .setNegativeButton("Đóng", null)
                .show();
    }

    private void cancelOrder() {
        progressBar.setVisibility(View.VISIBLE);

        orderRepository.cancelOrder(orderId).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                progressBar.setVisibility(View.GONE);

                if (success != null && success) {
                    Toast.makeText(OrderDetailActivity.this, 
                        "Đã hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                    loadOrderDetail(); // Reload to update status
                } else {
                    Toast.makeText(OrderDetailActivity.this, 
                        "Không thể hủy đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createZaloPayPayment() {
        if (currentOrder == null) {
            Toast.makeText(this, "Vui lòng chờ tải thông tin đơn hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        
        ZaloPayCreateRequest request = new ZaloPayCreateRequest(orderId);
        
        paymentApi.createZaloPayPayment(request).enqueue(new Callback<ZaloPayCreateResponse>() {
            @Override
            public void onResponse(Call<ZaloPayCreateResponse> call, Response<ZaloPayCreateResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.errorBody() != null) {
                    try {
                        String errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                if (response.isSuccessful() && response.body() != null) {
                    ZaloPayCreateResponse zaloPayResponse = response.body();
                    if (zaloPayResponse.getReturnCode() == 1) {
                        // Open ZaloPay payment URL in browser
                        String orderUrl = zaloPayResponse.getOrderUrl();
                        if (orderUrl != null && !orderUrl.isEmpty()) {
                            // Save orderId for deep link callback
                            getSharedPreferences("payment_prefs", MODE_PRIVATE)
                                .edit()
                                .putInt("pending_order_id", orderId)
                                .apply();
                            
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(orderUrl));
                            startActivity(browserIntent);
                            
                            Toast.makeText(OrderDetailActivity.this, 
                                "Đang chuyển đến trang thanh toán ZaloPay", 
                                Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OrderDetailActivity.this, 
                                "Không nhận được URL thanh toán", 
                                Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OrderDetailActivity.this, 
                            "Lỗi: " + zaloPayResponse.getReturnMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMsg = "Không thể tạo thanh toán. Code: " + response.code();
                    Toast.makeText(OrderDetailActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ZaloPayCreateResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderDetailActivity.this, 
                    "Lỗi kết nối: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload order when user returns from payment
        loadOrderDetail();
    }
}