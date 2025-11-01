package com.example.se1707_prm392_g2_petshop.ui.admin.chat;

public class AdminChatPresenter implements AdminChatContract.Presenter {

    private final AdminChatContract.View mView;

    public AdminChatPresenter(AdminChatContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Logic to load chat will go here
    }
}
