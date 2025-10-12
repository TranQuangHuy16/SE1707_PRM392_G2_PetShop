package com.example.se1707_prm392_g2_petshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.se1707_prm392_g2_petshop.ui.auth.welcome.WelcomeActivity;
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
            // Chuyển sang WelcomeActivity
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish(); // đóng MainActivity để không quay lại splash khi bấm Back
        }, SPLASH_DELAY);
    }

}