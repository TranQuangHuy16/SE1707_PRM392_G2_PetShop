package com.example.se1707_prm392_g2_petshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.cloudinary.android.Utils;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.ui.admin.AdminActivity;
import com.example.se1707_prm392_g2_petshop.ui.auth.welcome.WelcomeActivity;
import com.example.se1707_prm392_g2_petshop.ui.user.main.UserMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.se1707_prm392_g2_petshop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main); // layout splash nếu có (logo, tên app, v.v.)

        // Sử dụng Handler để delay 3 giây rồi chuyển màn hình
        new Handler().postDelayed(() -> {
            // Kiểm tra token trong SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
            String token = sharedPref.getString("jwt_token", null);
            String role = JwtUtil.getRoleFromToken(token);
            Intent intent;
            if (token != null && !token.isEmpty()) {
                // Nếu đã có token => chuyển sang HomeActivity
                if (role != null && role.equals("Admin")) {
                    intent = new Intent(MainActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, UserMainActivity.class);
                }
            } else {
                // Nếu chưa có token => chuyển sang WelcomeActivity
                intent = new Intent(MainActivity.this, WelcomeActivity.class);
            }

            startActivity(intent);
            finish(); // đóng Splash
        }, SPLASH_DELAY);
    }

}