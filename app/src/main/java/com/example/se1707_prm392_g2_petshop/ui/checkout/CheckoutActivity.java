package com.example.se1707_prm392_g2_petshop.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.adapter.CartAdapter;
import com.example.se1707_prm392_g2_petshop.data.dtos.CreateOrderRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Cart;
import com.example.se1707_prm392_g2_petshop.data.models.CartItem;
import com.example.se1707_prm392_g2_petshop.data.models.Order;
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress;
import com.example.se1707_prm392_g2_petshop.data.repositories.CartRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.OrderRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.ui.order.OrderDetailActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_ADDRESS = 100;

    private LinearLayout layoutAddress;
    private TextView tvAddressInfo, tvTotalAmount;
    private RecyclerView rvCartItems;
    private EditText etNote;
    private AppCompatButton btnCreateOrder;
    private ProgressBar progressBar;
    private ImageView btnBack;

    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private UserAddressRepository addressRepository;

    private Cart currentCart;
    private UserAddress selectedAddress;
    private List<UserAddress> userAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.setupEdgeToEdge(this);
        View rootView = findViewById(android.R.id.content);
        WindowInsetsUtil.applySystemBarInsets(rootView);

        initViews();
        loadCart();
        loadDefaultAddress();
    }

    private void initViews() {
        layoutAddress = findViewById(R.id.layoutAddress);
        tvAddressInfo = findViewById(R.id.tvAddressInfo);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvCartItems = findViewById(R.id.rvCartItems);
        etNote = findViewById(R.id.etNote);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);

        cartRepository = CartRepository.getInstance(this);
        orderRepository = OrderRepository.getInstance(this);
        addressRepository = UserAddressRepository.getInstance(this);

        btnBack.setOnClickListener(v -> finish());
        btnCreateOrder.setOnClickListener(v -> createOrder());
        layoutAddress.setOnClickListener(v -> showAddressSelectionDialog());
    }

    private void loadCart() {
        progressBar.setVisibility(View.VISIBLE);

        cartRepository.getMyCart().observe(this, new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                progressBar.setVisibility(View.GONE);

                if (cart != null && cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
                    currentCart = cart;
                    displayCart(cart);
                } else {
                    Toast.makeText(CheckoutActivity.this, 
                        "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void loadDefaultAddress() {
        addressRepository.getUserAddresses().observe(this, new Observer<List<UserAddress>>() {
            @Override
            public void onChanged(List<UserAddress> addresses) {
                Log.d("CheckoutActivity", "Addresses loaded: " + (addresses != null ? addresses.size() : "null"));
                
                userAddresses = addresses;
                
                if (addresses != null && !addresses.isEmpty()) {
                    // Find default address or use first one
                    for (UserAddress address : addresses) {
                        if (address.isDefault()) {
                            selectedAddress = address;
                            Log.d("CheckoutActivity", "Found default address: " + address.getAddressId());
                            break;
                        }
                    }
                    if (selectedAddress == null) {
                        selectedAddress = addresses.get(0);
                        Log.d("CheckoutActivity", "Using first address: " + selectedAddress.getAddressId());
                    }
                    displayAddress(selectedAddress);
                } else {
                    Log.w("CheckoutActivity", "No addresses found for user");
                    Toast.makeText(CheckoutActivity.this, 
                        "Bạn chưa có địa chỉ giao hàng. Vui lòng thêm địa chỉ!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showAddressSelectionDialog() {
        if (userAddresses == null || userAddresses.isEmpty()) {
            Toast.makeText(this, "Bạn chưa có địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create array of address strings for dialog
        String[] addressStrings = new String[userAddresses.size()];
        int selectedIndex = -1;
        
        for (int i = 0; i < userAddresses.size(); i++) {
            UserAddress address = userAddresses.get(i);
            addressStrings[i] = address.getAddressLine() + ", " +
                    address.getWard() + ", " +
                    address.getDistrict() + ", " +
                    address.getCity();
            
            // Mark current selected address
            if (selectedAddress != null && address.getAddressId() == selectedAddress.getAddressId()) {
                selectedIndex = i;
                addressStrings[i] = "✓ " + addressStrings[i];
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn địa chỉ giao hàng");
        builder.setSingleChoiceItems(addressStrings, selectedIndex, (dialog, which) -> {
            selectedAddress = userAddresses.get(which);
            displayAddress(selectedAddress);
            Log.d("CheckoutActivity", "Address selected: " + selectedAddress.getAddressId());
            dialog.dismiss();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void displayCart(Cart cart) {
        // Setup RecyclerView với CartAdapter (readonly mode)
        CartAdapter adapter = new CartAdapter(new CartAdapter.OnCartItemClickListener() {
            @Override
            public void onIncreaseClick(CartItem cartItem) {
                // Do nothing in checkout mode
            }

            @Override
            public void onDecreaseClick(CartItem cartItem) {
                // Do nothing in checkout mode
            }

            @Override
            public void onRemoveClick(CartItem cartItem) {
                // Do nothing in checkout mode
            }
        });
        adapter.submitList(cart.getCartItems());
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(adapter);

        // Display total
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalAmount.setText(formatter.format(cart.getTotalAmount()));
    }

    private void displayAddress(UserAddress address) {
        if (address != null) {
            String addressText = address.getAddressLine() + ", " +
                    address.getWard() + ", " +
                    address.getDistrict() + ", " +
                    address.getCity();
            tvAddressInfo.setText(addressText);
        }
    }

    private void createOrder() {
        if (currentCart == null) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedAddress == null) {
            Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        btnCreateOrder.setEnabled(false);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setAddressId(selectedAddress.getAddressId());
        request.setNote(etNote.getText().toString());

        orderRepository.createOrderFromCart(request).observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                progressBar.setVisibility(View.GONE);
                btnCreateOrder.setEnabled(true);

                if (order != null) {
                    Toast.makeText(CheckoutActivity.this, 
                        "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(CheckoutActivity.this, OrderDetailActivity.class);
                    intent.putExtra("orderId", order.getOrderId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CheckoutActivity.this, 
                        "Đặt hàng thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
