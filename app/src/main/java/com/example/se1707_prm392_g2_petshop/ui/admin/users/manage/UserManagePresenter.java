package com.example.se1707_prm392_g2_petshop.ui.admin.users.manage;

public class UserManagePresenter implements UserManageContract.Presenter{
    private final UserManageContract.View mView;

    public UserManagePresenter(UserManageContract.View mView) {
        this.mView = mView;
    }
}
