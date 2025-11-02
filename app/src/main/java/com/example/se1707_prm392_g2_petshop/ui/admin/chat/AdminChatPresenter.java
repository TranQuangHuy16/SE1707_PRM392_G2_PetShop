package com.example.se1707_prm392_g2_petshop.ui.admin.chat;

import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.User;
import com.example.se1707_prm392_g2_petshop.data.repositories.ChatRepository;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminChatPresenter implements AdminChatContract.Presenter {

    private final AdminChatContract.View mView;
    private final ChatRepository mRepository;

    public AdminChatPresenter(AdminChatContract.View mView, ChatRepository repository) {
        this.mView = mView;
        this.mRepository = repository;
        mView.setPresenter(this);
    }

    @Override
    public void start(int adminId) {
        loadChats(adminId); // giả sử adminId = 1
    }

    private void loadChats(int adminId) {
        mRepository.getRoomByAdminId(adminId).enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Chat> chatList = response.body();
                    fetchUsers(chatList);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Chat>> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }

    private void fetchUsers(ArrayList<Chat> chatList) {
        ArrayList<User> users = new ArrayList<>();
        if (chatList.isEmpty()) {
            mView.showChatList(chatList, users);
            return;
        }

        for (int i = 0; i < chatList.size(); i++) {
            int customerId = chatList.get(i).getCustomerId();
            final int index = i;
            mRepository.getUserById(customerId).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // đảm bảo thêm đúng vị trí
                        while (users.size() <= index) users.add(null);
                        users.set(index, response.body());

                        // Khi tất cả users đã load xong
                        boolean allLoaded = true;
                        for (User u : users) {
                            if (u == null) {
                                allLoaded = false;
                                break;
                            }
                        }
                        if (allLoaded) {
                            mView.showChatList(chatList, users);
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // xử lý lỗi nếu cần
                }
            });
        }
    }
}

