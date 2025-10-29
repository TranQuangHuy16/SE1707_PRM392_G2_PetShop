package com.example.se1707_prm392_g2_petshop.ui.admin.settings;

public class AdminSettingsPresenter implements AdminSettingsContract.Presenter {

    private final AdminSettingsContract.View mView;

    public AdminSettingsPresenter(AdminSettingsContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Logic for settings will go here
    }
}
