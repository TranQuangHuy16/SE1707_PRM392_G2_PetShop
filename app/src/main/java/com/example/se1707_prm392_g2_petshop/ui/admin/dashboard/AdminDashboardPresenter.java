package com.example.se1707_prm392_g2_petshop.ui.admin.dashboard;

import android.util.Log;

public class AdminDashboardPresenter implements AdminDashboardContract.Presenter {

    private final AdminDashboardContract.View mView;

    public AdminDashboardPresenter(AdminDashboardContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Presenter started
        Log.d("AdminDashboard", "Presenter started");
    }

    @Override
    public void onManageProductsClicked() {
        Log.d("AdminDashboard", "Manage Products clicked");
        mView.navigateToManageProducts();
    }

    @Override
    public void onManageUsersClicked() {
        Log.d("AdminDashboard", "Manage Users clicked");
        mView.navigateToManageUsers();
    }

    @Override
    public void onViewOrdersClicked() {
        Log.d("AdminDashboard", "View Orders clicked");
        mView.navigateToViewOrders();
    }
}
