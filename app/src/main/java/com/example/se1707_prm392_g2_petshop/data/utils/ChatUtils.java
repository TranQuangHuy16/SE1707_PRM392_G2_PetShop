package com.example.se1707_prm392_g2_petshop.data.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ChatUtils {

    public static boolean hasUnreadMessages(Context context, int chatRoomId, int lastMessageId) {
        SharedPreferences prefs = context.getSharedPreferences("chat_prefs", Context.MODE_PRIVATE);
        String lastReadIdStr = prefs.getString(String.valueOf(chatRoomId), "0");
        int lastReadId = 0;
        try {
            lastReadId = Integer.parseInt(lastReadIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return lastMessageId > lastReadId;
    }

    public static void markAsRead(Context context, int chatRoomId, int lastMessageId) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("chat_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString(String.valueOf(chatRoomId), String.valueOf(lastMessageId)).apply();
    }

}
