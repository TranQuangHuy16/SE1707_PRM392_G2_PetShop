package com.example.se1707_prm392_g2_petshop.ui.admin;

import android.os.Bundle;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cài đặt Toolbar làm ActionBar
        setSupportActionBar(binding.adminToolbar);

        // Tìm và khởi tạo NavController
        // ✅ Lấy NavController từ NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.admin_nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // ✅ Cài đặt DrawerLayout và NavigationView
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(binding.adminDrawerLayout)
                .build();

        // Cài đặt the Toolbar with NavController and AppBarConfiguration
        // ✅ Sử dụng NavigationUI để thiết lập Toolbar và DrawerLayout
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        // ✅ Sử dụng NavigationUI để thiết lập NavigationView và NavController
        NavigationUI.setupWithNavController(binding.adminNavView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the Up button by navigating using the NavController
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
