package com.example.se1707_prm392_g2_petshop.ui.user.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.bumptech.glide.Glide;
import com.example.se1707_prm392_g2_petshop.R;

import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.CartRepository;
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.databinding.ActivityUserMainBinding;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;
import com.example.se1707_prm392_g2_petshop.ui.chat.ChatActivity;
import com.example.se1707_prm392_g2_petshop.ui.userdetail.UserDetailActivity;
import com.google.android.material.navigation.NavigationView;

// Import cho Call, Callback, Response
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMainActivity extends AppCompatActivity {

    private static boolean cartNotificationSent = false;
    private ActivityUserMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private UserApi userApi;
    private UserAddressRepository addressRepository;

    // ✅ BƯỚC 1: Biến NavController thành biến toàn cục (class field)
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Fix notch & navigation bar
        WindowInsetsUtil.setupEdgeToEdge(this);
        View rootView = findViewById(android.R.id.content);
        WindowInsetsUtil.applySystemBarInsets(rootView);

        // Gắn Toolbar làm ActionBar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView drawerNavView = binding.drawerNavView;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_user_main);
        // Gán cho biến toàn cục
        navController = navHostFragment.getNavController();

        // Xử lý khi bấm vào item trong drawer
        drawerNavView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Handle Order menu items
            if (id == R.id.nav_my_order) {
                Intent intent = new Intent(this,
                        com.example.se1707_prm392_g2_petshop.ui.order.OrderListActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            if (id == R.id.nav_my_profile) {
                Intent intent = new Intent(this,
                        UserDetailActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            // Handle Address menu item
            if (id == R.id.nav_address) {
                Intent intent = new Intent(this,
                        com.example.se1707_prm392_g2_petshop.ui.address.list.AddressListActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            if (id == R.id.nav_chat) {
                Intent intent = new Intent(this,
                        ChatActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            // Let NavigationUI handle other items
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return handled;
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_notifications,
                R.id.navigation_cart,
                R.id.navigation_profile
        ).setOpenableLayout(drawerLayout).build();

        // ✅ BƯỚC 2: THÊM DÒNG NÀY
        // Dòng này liên kết Toolbar với NavController,
        // cho phép nó xử lý các sự kiện điều hướng (như tiêu đề, nút Up)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Dòng này liên kết BottomNav với NavController
        NavigationUI.setupWithNavController(binding.navView, navController);


        // Listener cho nút Log Out
        binding.btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = this.getSharedPreferences("auth_prefs", (int) Context.MODE_PRIVATE);
            JwtUtil.RemoveJwtTokenFromSharedPreferences(prefs);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Gọi API để lấy thông tin user
        userApi = RetrofitClient.getUserApi(this);
        // Khởi tạo Address Repository
        addressRepository = UserAddressRepository.getInstance(this);

        String id = JwtUtil.getSubFromToken(this);
        int userId = id != null ? Integer.parseInt(id) : -1;

        // Cập nhật lệnh gọi loadUserInfo (đã bỏ NavController vì nó là biến toàn cục)
        loadUserInfo(userId, drawerNavView);

        // Gọi hàm mới để tải địa chỉ
        loadDefaultAddress(userId);

        // Logic thông báo giỏ hàng
        if (!cartNotificationSent) {
            cartNotificationSent = true;
            checkCartAndNotify();
        }
    }

    // Cập nhật hàm (đã bỏ tham số NavController)
    private void loadUserInfo(int userId, NavigationView drawerNavView) {
        if (userId == -1) return;

        Call<User> call = userApi.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    // A. Cập nhật Header của Drawer (Giữ nguyên)
                    View headerView = drawerNavView.getHeaderView(0);
                    TextView tvName = headerView.findViewById(R.id.tv_user_name);
                    TextView tvEmail = headerView.findViewById(R.id.tv_user_email);
                    ImageView imgAvatar = headerView.findViewById(R.id.img_avatar);

                    if (tvName != null) tvName.setText(user.getFullName());
                    if (tvEmail != null) tvEmail.setText(user.getEmail());

                    if (imgAvatar != null) {
                        Glide.with(UserMainActivity.this)
                                .load(user.getImgAvatarl())
                                .placeholder(R.drawable.ic_profile)
                                .circleCrop()
                                .into(imgAvatar);
                    }

                    // B. Cập nhật Avatar trên Toolbar
                    if (binding.imgToolbarAvatar != null) {
                        Glide.with(UserMainActivity.this)
                                .load(user.getImgAvatarl())
                                .placeholder(R.drawable.ic_profile)
                                .error(R.drawable.ic_profile)
                                .circleCrop()
                                .into(binding.imgToolbarAvatar);

                        // Thêm click listener để đi đến trang profile
                        binding.imgToolbarAvatar.setOnClickListener(v -> {
                            binding.navView.setSelectedItemId(R.id.navigation_profile);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Tải địa chỉ mặc định
    private void loadDefaultAddress(int userId) {
        if (userId == -1) return;

        Call<UserAddress> call = addressRepository.getAddressDefaultByUserId(userId);
        call.enqueue(new Callback<UserAddress>() {
            @Override
            public void onResponse(Call<UserAddress> call, Response<UserAddress> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserAddress defaultAddress = response.body();
                    if (binding.tvDeliveryAddress != null) {
                        binding.tvDeliveryAddress.setText(defaultAddress.getAddressLine());
                    }
                } else {
                    if (binding.tvDeliveryAddress != null) {
                        binding.tvDeliveryAddress.setText("No default address");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserAddress> call, Throwable t) {
                if (binding.tvDeliveryAddress != null) {
                    binding.tvDeliveryAddress.setText("Can't load address");
                }
                t.printStackTrace();
            }
        });
    }

    // Hàm tạo kênh thông báo
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "cart_channel";
            CharSequence name = "Cart Notifications";
            String description = "Thông báo giỏ hàng";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Hàm kiểm tra giỏ hàng và gửi thông báo
    private void checkCartAndNotify() {
        createNotificationChannel();
        CartRepository cartRepository = CartRepository.getInstance(this);

        cartRepository.getMyCart().observe(this, cart -> {
            if (cart != null && cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
                int count = cart.getCartItems().size();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "cart_channel")
                        .setSmallIcon(R.drawable.ic_cart)
                        .setContentTitle("Giỏ hàng của bạn")
                        .setContentText("Bạn có " + count + " sản phẩm trong giỏ hàng. Hãy hoàn tất đơn hàng nhé!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    notificationManager.notify(1001, builder.build());
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Dùng biến toàn cục
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}