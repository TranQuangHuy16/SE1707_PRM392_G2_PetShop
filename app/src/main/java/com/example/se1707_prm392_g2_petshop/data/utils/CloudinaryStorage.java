package com.example.se1707_prm392_g2_petshop.data.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryStorage {
    // 🔹 Cấu hình Cloudinary (bạn có thể load từ file config sau này)
    private static final String CLOUD_NAME = "dqvu7cnjc";
    private static final String API_KEY = "788354224923518";
    private static final String API_SECRET = "5r_09IjZHvVfYBaT6H2AYHbci3k";

    private static Cloudinary cloudinary;

    // ✅ Singleton Cloudinary instance (chỉ khởi tạo 1 lần)
    private static Cloudinary getCloudinaryInstance() {
        if (cloudinary == null) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", CLOUD_NAME);
            config.put("api_key", API_KEY);
            config.put("api_secret", API_SECRET);
            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }

    // ✅ Hàm upload ảnh (chạy async, trả về qua callback)
    public static void uploadImage(ContentResolver resolver, Uri imageUri, CloudinaryCallback callback) {
        new Thread(() -> {
            try {
                Cloudinary cloud = getCloudinaryInstance();

                try (InputStream inputStream = resolver.openInputStream(imageUri)) {
                    Map uploadResult = cloud.uploader().upload(inputStream, ObjectUtils.emptyMap());
                    String imageUrl = (String) uploadResult.get("secure_url");
                    Log.d("CLOUDINARY_UTIL", "Uploaded: " + imageUrl);
                    callback.onSuccess(imageUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(e);
            }
        }).start();
    }

    // cách sử dụng
    /* lấy ảnh từ thư viện điện thoại là Uri img
    * // ✅ Gọi util để upload
            CloudinaryUtil.uploadImage(getContentResolver(), imageUri, new CloudinaryUtil.CloudinaryCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    runOnUiThread(() -> {
                        Log.d("RESULT", "Image URL: " + imageUrl);
                        tvTest.setText("Upload thành công:\n" + imageUrl); lấy string link hình ảnh
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Log.e("RESULT", "Upload lỗi: " + e.getMessage());
                        tvTest.setText("Upload lỗi!");
                    });
                }
            });
    * */

    public interface CloudinaryCallback {
        void onSuccess(String imageUrl);
        void onError(Exception e);
    }
}

