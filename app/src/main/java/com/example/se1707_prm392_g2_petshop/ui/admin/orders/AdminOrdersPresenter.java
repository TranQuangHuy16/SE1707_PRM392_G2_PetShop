package com.example.se1707_prm392_g2_petshop.ui.admin.orders;

public class AdminOrdersPresenter implements AdminOrdersContract.Presenter {

    private final AdminOrdersContract.View mView;

    public AdminOrdersPresenter(AdminOrdersContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Logic to load orders will go here
    }
}
