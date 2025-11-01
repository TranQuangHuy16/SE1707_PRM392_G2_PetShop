package com.example.se1707_prm392_g2_petshop.data.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class JwtUtil {
    public static String getUserIdFromToken(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
            String jwtToken = prefs.getString("jwt_token", null);
            if (jwtToken == null) {
                return null;
            }
            return getClaimFromToken(jwtToken, "nameid");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSubFromToken(Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
            String jwtToken = prefs.getString("jwt_token", null);
            if (jwtToken == null) {
                return null;
            }
            return getClaimFromToken(jwtToken, "sub");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRoleFromToken(String jwtToken) {
        // Corrected based on backend code: the claim key is "role".
        return getClaimFromToken(jwtToken, "role");
    }

    private static String getClaimFromToken(String jwtToken, String claimKey) {
        try {
            if (jwtToken == null) {
                return null;
            }
            String[] parts = jwtToken.split("\\.");
            if (parts.length < 2) {
                return null;
            }
            String payload = parts[1];
            String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));
            JSONObject jsonObject = new JSONObject(decodedPayload);
            return jsonObject.optString(claimKey, null);
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
