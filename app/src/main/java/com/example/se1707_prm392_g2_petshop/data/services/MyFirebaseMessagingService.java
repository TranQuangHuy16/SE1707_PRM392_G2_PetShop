package com.example.se1707_prm392_g2_petshop.data.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.se1707_prm392_g2_petshop.R;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil;
import com.example.se1707_prm392_g2_petshop.ui.chat.ChatActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(String token) {
        String idString = JwtUtil.getSubFromToken(this);
        int userId = idString != null ? Integer.parseInt(idString) : -1;

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String fcmToken = task.getResult();
                        UserRepository repo = new UserRepository(this);
                        repo.updateFcmToken(userId, fcmToken);
                    } else {
                        Log.e(TAG, "Failed to get FCM token", task.getException());
                    }
                });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Nếu đang ở trong ChatActivity thì không hiện thông báo chat
        if (ChatActivity.isVisible) return;

        // 🔹 Kiểm tra nếu đây là thông báo chỉ có title & body (không có receiverId)
        if (remoteMessage.getData() == null || !remoteMessage.getData().containsKey("receiverId")) {
            showSimpleNotification(remoteMessage);
            return;
        }

        // 🔹 Nếu có receiverId => xử lý như cũ
        String idString = JwtUtil.getSubFromToken(this);
        if (idString == null) {
            Log.w(TAG, "Không thể lấy userId từ JWT, bỏ qua thông báo");
            return;
        }

        int currentUserId = Integer.parseInt(idString);

        String receiverIdStr = remoteMessage.getData().get("receiverId");
        int receiverId = -1;
        try {
            if (receiverIdStr != null) receiverId = Integer.parseInt(receiverIdStr);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Lỗi parse receiverId từ data payload", e);
            return;
        }

        if (receiverId == -1 || currentUserId != receiverId) {
            Log.d(TAG, "User hiện tại (" + currentUserId + ") không phải người nhận (" + receiverId + "), bỏ qua thông báo");
            return;
        }

        Log.d(TAG, "Hiển thị thông báo chat cho userId=" + currentUserId);
        showChatNotification(remoteMessage);
    }
    private void showChatNotification(RemoteMessage remoteMessage) {
        String title = "Tin nhắn mới";
        String body = remoteMessage.getNotification() != null
                ? remoteMessage.getNotification().getBody()
                : remoteMessage.getData().get("body");

        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "chat_channel")
                .setSmallIcon(R.drawable.ic_chat)
                .setContentTitle(title)
                .setContentText(body != null ? body : "Bạn có tin nhắn mới")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo_app)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "chat_channel",
                    "Chat Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void showSimpleNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification() != null
                ? remoteMessage.getNotification().getTitle()
                : remoteMessage.getData().get("title");

        String body = remoteMessage.getNotification() != null
                ? remoteMessage.getNotification().getBody()
                : remoteMessage.getData().get("body");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "general_channel")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title != null ? title : "Thông báo")
                .setContentText(body != null ? body : "")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo_app)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "general_channel",
                    "General Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }


}
