package com.example.se1707_prm392_g2_petshop.data.retrofit;

import android.content.Context;
import android.os.Build;

import com.example.se1707_prm392_g2_petshop.data.api.AuthApi;
import com.example.se1707_prm392_g2_petshop.data.api.CategoryApi;
import com.example.se1707_prm392_g2_petshop.data.api.ChatApi;
import com.example.se1707_prm392_g2_petshop.data.api.UserAddressApi;
import com.example.se1707_prm392_g2_petshop.data.api.ProductApi;
import com.example.se1707_prm392_g2_petshop.data.api.UserApi;
import com.example.se1707_prm392_g2_petshop.data.constants.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    public static Retrofit getInstance(Context context) {
        if (instance == null) {
            // Define a custom DateTimeFormatter for LocalDateTime
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            }

            Gson gson = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class,
                                (JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
                                        LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .create();
            }


            // Create OkHttpClient with an interceptor for authentication
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context)) // Thêm interceptor ở đây
                    .build();

            instance = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL) // Replace with your base URL
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return instance;
    }

    public static AuthApi getAuthApi(Context context) {
        return getInstance(context).create(AuthApi.class);
    }

    public static ChatApi getChatApi(Context context) {
        return getInstance(context).create(ChatApi.class);
    }

    public static UserApi getUserApi(Context context) {
        return getInstance(context).create(UserApi.class);
    }

    public static UserAddressApi getUserAddressApi(Context context) {
        return getInstance(context).create(UserAddressApi.class);
    public static ProductApi getProductApi(Context context) {
        return getInstance(context).create(ProductApi.class);
    }

    public static CategoryApi getCategoryApi(Context context) {
        return getInstance(context).create(CategoryApi.class);
    }
}
