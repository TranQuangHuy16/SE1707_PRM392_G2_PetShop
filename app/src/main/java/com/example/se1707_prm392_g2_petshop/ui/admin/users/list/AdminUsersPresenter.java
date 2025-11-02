package com.example.se1707_prm392_g2_petshop.ui.admin.users;

public class AdminUsersPresenter implements AdminUsersContract.Presenter {

    private final AdminUsersContract.View mView;

    public AdminUsersPresenter(AdminUsersContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Logic to load users will go here
    }
}
