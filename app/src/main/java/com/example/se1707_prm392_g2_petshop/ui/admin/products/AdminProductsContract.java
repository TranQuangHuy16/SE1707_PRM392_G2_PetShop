package com.example.se1707_prm392_g2_petshop.ui.admin.products;

public interface AdminProductsContract {
    interface View {
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void start();
    }
}
