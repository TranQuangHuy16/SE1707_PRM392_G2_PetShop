package com.example.se1707_prm392_g2_petshop.ui.user.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.databinding.ActivityUserMainBinding;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.google.android.material.navigation.NavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMainActivity extends AppCompatActivity {

    private ActivityUserMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Fix notch & navigation bar
        WindowInsetsUtil.setupEdgeToEdge(this);
        View rootView = findViewById(android.R.id.content);
        WindowInsetsUtil.applySystemBarInsets(rootView);

//        // ✅ Gắn Toolbar làm ActionBar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView drawerNavView = binding.drawerNavView;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_user_main);
        NavController navController = navHostFragment.getNavController();

        // Xử lý khi bấm vào item trong drawer
        drawerNavView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Handle Order menu items
            if (id == R.id.nav_my_order) {
                android.content.Intent intent = new android.content.Intent(this,
                        com.example.se1707_prm392_g2_petshop.ui.order.OrderListActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            // Handle Address menu item
            if (id == R.id.nav_address) {
                android.content.Intent intent = new android.content.Intent(this,
                        com.example.se1707_prm392_g2_petshop.ui.address.list.AddressListActivity.class);
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

        binding.imgMenuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationUI.setupWithNavController(binding.navView, navController);

        // ✅ Gọi API để lấy thông tin user
        userApi = RetrofitClient.getUserApi(this);
        String id = JwtUtil.getSubFromToken(this);
        int userId = id != null ? Integer.parseInt(id) : -1;
        loadUserInfo(userId, drawerNavView);
    }

    private void loadUserInfo(int userId, NavigationView drawerNavView) {
        Call<User> call = userApi.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    View headerView = drawerNavView.getHeaderView(0);
                    TextView tvName = headerView.findViewById(R.id.tv_user_name);
                    TextView tvEmail = headerView.findViewById(R.id.tv_user_email);
                    ImageView imgAvatar = headerView.findViewById(R.id.img_avatar);

                    tvName.setText(user.getFullName());
                    tvEmail.setText(user.getEmail());

                    Glide.with(UserMainActivity.this)
                            .load(user.getImgAvatarl())
                            .placeholder(R.drawable.ic_profile)
                            .circleCrop()
                            .into(imgAvatar);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_user_main))
                .getNavController();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
