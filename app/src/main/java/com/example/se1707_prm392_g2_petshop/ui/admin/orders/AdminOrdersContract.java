package com.example.se1707_prm392_g2_petshop.ui.admin.orders;

public interface AdminOrdersContract {
    interface View {
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void start();
    }
}
