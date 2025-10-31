package com.example.se1707_prm392_g2_petshop.ui.admin.dashboard;

public interface AdminDashboardContract {
    interface View {
        void setPresenter(Presenter presenter);
        void navigateToManageProducts();
        void navigateToManageUsers();
        void navigateToViewOrders();
        void showTotalUsers(int count);
        void showTotalProducts(int count);
        void showTotalOrders(int count);
        void showLoading();
        void hideLoading();
        void showError(String message);
    }

    interface Presenter {
        void start();
        void loadDashboardData();
        void onManageProductsClicked();
        void onManageUsersClicked();
        void onViewOrdersClicked();
    }
}
