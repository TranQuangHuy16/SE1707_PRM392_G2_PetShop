package com.example.se1707_prm392_g2_petshop.ui.address.list

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddressListPresenter(
    private val view: AddressListContract.View,
    private val repo: UserAddressRepository,
    private val context: Context
) : AddressListContract.Presenter {

    private val composite = CompositeDisposable()
    private val lifecycleOwner = context as LifecycleOwner

    override fun loadAddresses() {
        view.showLoading(true)
        repo.getUserAddresses().observeForever { result ->
            view.showLoading(false)
            if (result != null) view.showAddresses(result)
            else view.showError("Không thể tải danh sách địa chỉ.")
        }
    }

    override fun deleteAddress(address: UserAddress) {
        view.showLoading(true)
        // Dùng observe(lifecycleOwner)
        repo.deleteUserAddress(address.addressId).observe(lifecycleOwner) { result ->
            view.showLoading(false)
            if (result != null) {
                // Nếu thành công, báo view
                view.showDeleteSuccess("Đã xóa địa chỉ thành công.")
            } else {
                view.showError("Xóa địa chỉ thất bại.")
            }
        }
    }

    override fun onDestroy() {
        composite.clear()
    }
}
