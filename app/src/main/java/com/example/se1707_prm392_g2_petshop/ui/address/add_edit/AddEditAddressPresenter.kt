package com.example.se1707_prm392_g2_petshop.ui.address.add_edit

import android.annotation.SuppressLint
import android.content.Context
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UserAddressRequest
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
class AddEditAddressPresenter(
    private val view: AddEditAddressContract.View,
    private val repo: UserAddressRepository,
    private val context: Context
) : AddEditAddressContract.Presenter {

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun getCurrentLocation() {
        locationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                view.updateMap(it.latitude, it.longitude)
            } else {
                view.showMessage("Không thể lấy vị trí hiện tại.")
            }
        }
    }

    override fun onMapClicked(lat: Double, lon: Double) {
        view.updateMap(lat, lon)
    }

    override fun saveAddress(existing: UserAddress?, request: UserAddressRequest) {
        view.showLoading(true)
        if (existing == null) {
            repo.createUserAddress(request).observeForever { res ->
                view.showLoading(false)
                if (res != null) {
                    view.showMessage("Đã thêm địa chỉ thành công.")
                    view.finishView()
                } else {
                    view.showMessage("Thêm địa chỉ thất bại.")
                }
            }
        } else {
            repo.updateUserAddress(existing.addressId, request).observeForever { res ->
                view.showLoading(false)
                if (res != null) {
                    view.showMessage("Cập nhật địa chỉ thành công.")
                    view.finishView()
                } else {
                    view.showMessage("Cập nhật địa chỉ thất bại.")
                }
            }
        }
    }

    override fun onDestroy() {}
}
