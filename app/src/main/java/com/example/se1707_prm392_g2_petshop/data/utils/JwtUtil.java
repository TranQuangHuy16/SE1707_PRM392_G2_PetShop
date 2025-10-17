package com.example.se1707_prm392_g2_petshop.data.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class JwtUtil {
    public static String getUserIdFromToken(Context context) {
        try {
            // 1️⃣ Lấy token từ SharedPreferences
            SharedPreferences prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
            String jwtToken = prefs.getString("jwt_token", null);
            if (jwtToken == null) {
                return null; // Chưa đăng nhập
            }

            // 2️⃣ Giải mã phần payload của token
            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) return null;

            String payload = parts[1];
            String decodedPayload = new String(android.util.Base64.decode(payload, android.util.Base64.URL_SAFE));
            org.json.JSONObject jsonObject = new org.json.JSONObject(decodedPayload);

            // 3️⃣ Trích xuất userId từ "nameid"
            return jsonObject.optString("nameid", null);

        } catch (org.json.JSONException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSubFromToken(Context context) {
        try {
            // 1️⃣ Lấy token từ SharedPreferences
            SharedPreferences prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
            String jwtToken = prefs.getString("jwt_token", null);
            if (jwtToken == null) {
                return null; // Chưa đăng nhập
            }

            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) return null;

            String payload = parts[1];
            String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));
            JSONObject jsonObject = new JSONObject(decodedPayload);

            return jsonObject.getString("sub");  // "sub" là UserID
        } catch (JSONException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void SaveJwtTokenToSharedPreferences(String jwtToken, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", jwtToken);
        editor.apply();
    }

    public static void RemoveJwtTokenFromSharedPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("jwt_token");
        editor.apply();
    }
}
