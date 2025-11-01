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
import com.example.se1707_prm392_g2_petshop.data.api.CartApi;
import com.example.se1707_prm392_g2_petshop.data.api.PaymentApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    public static Retrofit getInstance(Context context) {
        if (instance == null) {
            // Custom deserializer for Date to handle .NET DateTime format
            JsonDeserializer<Date> dateDeserializer = (json, type, ctx) -> {
                try {
                    String dateString = json.getAsString();
                    // Handle .NET DateTime format: "2025-10-30T15:32:26.2898371"
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.US);
                    return sdf.parse(dateString);
                } catch (Exception e) {
                    try {
                        // Fallback to ISO 8601 format with timezone
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                        return sdf.parse(json.getAsString());
                    } catch (Exception ex) {
                        return null;
                    }
                }
            };

            GsonBuilder gsonBuilder = new GsonBuilder()
                    .registerTypeAdapter(Date.class, dateDeserializer);

            // Add LocalDateTime deserializer for Android O and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                gsonBuilder.registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, typeOfT, context1) ->
                                LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }

            // Create Logging Interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            Gson gson = gsonBuilder.create();

            // Create OkHttpClient with an interceptor for authentication
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context)) // Thêm interceptor ở đây
                    .addInterceptor(loggingInterceptor) // Thêm logging interceptor
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
    }
    public static ProductApi getProductApi(Context context) {
        return getInstance(context).create(ProductApi.class);
    }

    public static CategoryApi getCategoryApi(Context context) {
        return getInstance(context).create(CategoryApi.class);
    }

    public static CartApi getCartApi(Context context) {
        return getInstance(context).create(CartApi.class);
    }

    public static PaymentApi getPaymentApi(Context context) {
        return getInstance(context).create(PaymentApi.class);
    }
}
