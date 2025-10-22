package com.example.se1707_prm392_g2_petshop.ui.map

import com.example.se1707_prm392_g2_petshop.data.models.UserAddress

interface MapContract {
    interface View {
        fun showAddress(address: UserAddress)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadDefaultAddress(userId: Int)
    }
}