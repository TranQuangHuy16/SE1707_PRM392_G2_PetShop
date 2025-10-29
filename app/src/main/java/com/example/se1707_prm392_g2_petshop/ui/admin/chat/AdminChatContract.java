package com.example.se1707_prm392_g2_petshop.ui.admin.chat;

public interface AdminChatContract {
    interface View {
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void start();
    }
}
