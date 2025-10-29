package com.example.se1707_prm392_g2_petshop.ui.admin.settings;

public interface AdminSettingsContract {
    interface View {
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void start();
    }
}
