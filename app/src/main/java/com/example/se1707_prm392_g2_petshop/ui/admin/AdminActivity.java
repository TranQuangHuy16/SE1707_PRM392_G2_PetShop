package com.example.se1707_prm392_g2_petshop.ui.admin;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.databinding.ActivityAdminBinding;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cài đặt Toolbar làm ActionBar
        setSupportActionBar(binding.adminToolbar);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_nav_host_fragment);
        if (navHostFragment == null) throw new IllegalStateException("NavHostFragment not found!");
        navController = navHostFragment.getNavController();

        // 2️⃣ Cấu hình các top-level (hiển thị Drawer icon)
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_admin_dashboard,
                R.id.nav_admin_products,
                R.id.nav_admin_orders,
                R.id.nav_admin_users,
                R.id.nav_admin_chat,
                R.id.nav_admin_settings
        ).setOpenableLayout(binding.adminDrawerLayout).build();

        // 3️⃣ Gắn Toolbar và Drawer
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.adminNavView, navController);

        // 4️⃣ Custom xử lý click Drawer
        setupDrawerNavigation();
    }

    private void setupDrawerNavigation() {
        binding.adminNavView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            boolean handled = false;

            // ✅ Nếu là Dashboard → clear toàn bộ backstack và navigate lại Dashboard
            if (id == R.id.nav_admin_dashboard) {
                navController.popBackStack(R.id.nav_admin_dashboard, true);
                navController.navigate(R.id.nav_admin_dashboard);
                handled = true;
            } else {
                handled = NavigationUI.onNavDestinationSelected(item, navController);
            }

            // Đóng Drawer nếu xử lý xong
            if (handled) {
                binding.adminDrawerLayout.closeDrawers();
            }
            return handled;
        });

        // Optional: log để debug
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            System.out.println("🔹 Navigated to: " + destination.getLabel());
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Xử lý nút "Back" hoặc "Menu"
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
