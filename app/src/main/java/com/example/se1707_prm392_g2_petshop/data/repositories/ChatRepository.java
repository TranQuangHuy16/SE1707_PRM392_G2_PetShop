package com.example.se1707_prm392_g2_petshop.data.repositories;


import retrofit2.Call;
import com.example.se1707_prm392_g2_petshop.data.api.ChatApi;
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.SendMessageRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.SendMessageResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import java.util.ArrayList;

public class ChatRepository {
    private ChatApi chatApi;
    public ChatRepository(ChatApi chatApi) {
        this.chatApi = chatApi;
    }

    public Call<Chat> getRoomByCustomerId(int customerId) {
        return chatApi.getRoomByCustomerId(customerId);
    }

    public Call<ArrayList<Chat>> getRoomByAdminId(int adminId) {
        return chatApi.getRoomByAdminId(adminId);
    }

    public Call<SendMessageResponse> sendMessage(SendMessageRequest request) {
        return chatApi.sendMessage(request);
    }

    public Call<User> getUserById(int userId) {
        return chatApi.getUserById(userId);
    }
}
