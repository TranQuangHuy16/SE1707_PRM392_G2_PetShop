package com.example.se1707_prm392_g2_petshop.ui.admin.chat;
import com.example.se1707_prm392_g2_petshop.data.models.Chat;
import com.example.se1707_prm392_g2_petshop.data.models.User;

import java.util.ArrayList;

public interface AdminChatContract {
    interface View {
        void setPresenter(Presenter presenter);
        void showChatList(ArrayList<Chat> chats, ArrayList<User> users);
    }

    interface Presenter {
        void start(int adminId);
    }
}

