package com.example.se1707_prm392_g2_petshop.ui.map

import com.example.se1707_prm392_g2_petshop.data.models.UserAddress
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapPresenter(
    private val view: MapContract.View,
    private val repository: UserAddressRepository
) : MapContract.Presenter {

    override fun loadDefaultAddress(userId: Int) {
        repository.getAddressDefaultByUserId(userId).enqueue(object : Callback<UserAddress> {
            override fun onResponse(call: Call<UserAddress>, response: Response<UserAddress>) {
                val address = response.body()
                if (response.isSuccessful && address != null) {
                    view.showAddress(address)
                } else {
                    view.showError("Không tìm thấy địa chỉ mặc định")
                }
            }

            override fun onFailure(call: Call<UserAddress>, t: Throwable) {
                view.showError("Lỗi kết nối: ${t.message}")
            }
        })
    }
}