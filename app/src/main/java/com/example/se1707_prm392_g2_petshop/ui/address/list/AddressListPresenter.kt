package com.example.se1707_prm392_g2_petshop.ui.address.list

import android.content.Context
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddressListPresenter(
    private val view: AddressListContract.View,
    private val repo: UserAddressRepository,
    private val context: Context
) : AddressListContract.Presenter {

    private val composite = CompositeDisposable()

    override fun loadAddresses() {
        view.showLoading(true)
        repo.getUserAddresses().observeForever { result ->
            view.showLoading(false)
            if (result != null) view.showAddresses(result)
            else view.showError("Không thể tải danh sách địa chỉ.")
        }
    }

    override fun onDestroy() {
        composite.clear()
    }
}
