package com.example.se1707_prm392_g2_petshop.data.retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.se1707_prm392_g2_petshop.ui.auth.login.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private Context context;
    private static final String TAG = "AuthInterceptor";
    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", null);

        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();

        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        Request newRequest = builder.build();

        Response response = chain.proceed(newRequest);

        // Nếu server trả về lỗi 401 → token hết hạn hoặc không hợp lệ
        if (response.code() == 401) {
            Log.w(TAG, "Token hết hạn hoặc không hợp lệ. Chuyển về LoginActivity...");

            // Xóa token khỏi SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("jwt_token");
            editor.apply();

            // Chuyển hướng về màn hình đăng nhập
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        return response;
    }
}
