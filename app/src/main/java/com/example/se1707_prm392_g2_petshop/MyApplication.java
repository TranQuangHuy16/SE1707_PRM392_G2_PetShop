package com.example.se1707_prm392_g2_petshop;

import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // ⚙️ Cực kỳ quan trọng: phải khởi tạo ở đây
        AndroidThreeTen.init(this);
    }
}
