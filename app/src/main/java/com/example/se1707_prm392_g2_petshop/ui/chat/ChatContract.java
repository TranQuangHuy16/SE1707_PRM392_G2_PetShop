package com.example.se1707_prm392_g2_petshop.ui.chat;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.SendMessageRequest;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.User;

public interface ChatContract {
    interface View {
        void onGetRoomByCustomerIdSuccess(Chat chat);
        void onGetRoomByCustomerIdError(String message);

        void onSendMessageSuccess(String message);
        void onSendMessageError(String message);

        void onGetUserByIdSuccess(User user);
        void onGetUserByIdError(String message);
        void onFailure(String message);

    }

    interface Presenter {
        void getRoomByCustomerId(int customerId);
        void sendMessage(SendMessageRequest request);
        void getUserById(int id);
        void receiveMessage();
    }
}
