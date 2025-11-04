package com.example.se1707_prm392_g2_petshop.ui.address.add_edit

import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UserAddressRequest
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress

interface AddEditAddressContract {
    interface View {
        fun showAddress(address: UserAddress)
        fun showLoading(isLoading: Boolean)
        fun showMessage(message: String)
        fun finishView()
        fun updateMap(lat: Double, lon: Double)
    }

    interface Presenter {
        fun saveAddress(existing: UserAddress?, request: UserAddressRequest)
        fun getCurrentLocation()
        fun onMapClicked(lat: Double, lon: Double)
        fun onDestroy()
    }
}
