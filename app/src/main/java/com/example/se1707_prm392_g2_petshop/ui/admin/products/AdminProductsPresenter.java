package com.example.se1707_prm392_g2_petshop.ui.admin.products;

public class AdminProductsPresenter implements AdminProductsContract.Presenter {

    private final AdminProductsContract.View mView;

    public AdminProductsPresenter(AdminProductsContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Logic to load products will go here
    }
}
