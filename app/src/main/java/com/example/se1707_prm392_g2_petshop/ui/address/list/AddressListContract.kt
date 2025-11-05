package com.example.se1707_prm392_g2_petshop.ui.address.list

import com.example.se1707_prm392_g2_petshop.data.models.UserAddress

interface AddressListContract {
    interface View {
        fun showAddresses(addresses: List<UserAddress>)
        fun showLoading(isLoading: Boolean)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadAddresses()
        fun onDestroy()
    }
}
