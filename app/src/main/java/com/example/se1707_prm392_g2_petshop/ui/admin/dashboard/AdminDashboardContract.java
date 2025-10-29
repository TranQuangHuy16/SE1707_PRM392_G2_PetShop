package com.example.se1707_prm392_g2_petshop.ui.admin.dashboard;

public interface AdminDashboardContract {
    interface View {
        void setPresenter(Presenter presenter);
        void navigateToManageProducts();
        void navigateToManageUsers();
        void navigateToViewOrders();
    }

    interface Presenter {
        void start();
        void onManageProductsClicked();
        void onManageUsersClicked();
        void onViewOrdersClicked();
    }
}
