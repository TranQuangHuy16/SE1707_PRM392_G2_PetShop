package com.example.se1707_prm392_g2_petshop.data.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

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

        return chain.proceed(newRequest);
    }
}
