package com.example.se1707_prm392_g2_petshop.ui.chat;

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.SendMessageRequest;
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.SendMessageResponse;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.ChatRepository;
import com.example.se1707_prm392_g2_petshop.data.repositories.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View view;
    private ChatRepository repository;
    public ChatPresenter(ChatContract.View view, ChatRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getRommByCustomerId(int customerId) {
        repository.getRoomByCustomerId(customerId).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onGetRoomByCustomerIdSuccess(response.body());
                } else {
                    view.onGetRoomByCustomerIdError("Get room failed");
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                view.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void sendMessage(SendMessageRequest request) {
        repository.sendMessage(request).enqueue(new Callback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onSendMessageSuccess("Send message success");
                } else {
                    view.onSendMessageError("Send message failed");
                }
            }

            @Override
            public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                view.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void getUserById(int id) {
        repository.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    view.onGetUserByIdSuccess(response.body());
                } else {
                    view.onGetUserByIdError("Get user failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                view.onFailure(t.getMessage());
            }
        });
    }


    @Override
    public void receiveMessage() {

    }

}
