package com.example.se1707_prm392_g2_petshop.data.utils;

import android.content.SharedPreferences;

public class JwtUtil {
    public static void SaveJwtTokenToSharedPreferences(String jwtToken, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", jwtToken);
        editor.apply();
    }
}
