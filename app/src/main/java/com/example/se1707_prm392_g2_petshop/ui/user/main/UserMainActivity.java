package com.example.se1707_prm392_g2_petshop.ui.user.main;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.databinding.ActivityUserMainBinding;
import com.example.se1707_prm392_g2_petshop.data.utils.WindowInsetsUtil;
import com.google.android.material.navigation.NavigationView;

public class UserMainActivity extends AppCompatActivity {

    private ActivityUserMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

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
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Tắt chữ "Home"
        }

        DrawerLayout drawerLayout = binding.drawerLayout;
        NavigationView drawerNavView = binding.drawerNavView;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_user_main);
        NavController navController = navHostFragment.getNavController();

        // Handle drawer menu item clicks
        drawerNavView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_my_order) {
                android.content.Intent intent = new android.content.Intent(this, 
                    com.example.se1707_prm392_g2_petshop.ui.order.OrderListActivity.class);
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

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        // Don't use NavigationUI.setupWithNavController for drawer because we have custom listener above
        // NavigationUI.setupWithNavController(drawerNavView, navController);
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
