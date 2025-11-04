package com.example.se1707_prm392_g2_petshop.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.RouteRequest
import com.example.se1707_prm392_g2_petshop.data.dtos.responses.RouteResponse
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapPresenter(
    private val repository: UserAddressRepository // Sử dụng Repository (Java) đã có
) : MapContract.Presenter {

    private var view: MapContract.View? = null
    private var routeLiveData: LiveData<RouteResponse>? = null
    private var routeObserver: Observer<RouteResponse>? = null

    // Biến tạm để lưu trữ tọa độ
    private var userAddress: UserAddress? = null
    private var adminAddress: UserAddress? = null

    override fun attachView(view: MapContract.View) {
        this.view = view
    }

    // --- HÀM MỚI ---
    override fun loadRouteFromUserToAdmin(userId: Int) {
        view?.clearMapMarkers()

        // 2. Hiển thị loading
        view?.showLoading()

        // 1. Bắt đầu chuỗi: Lấy địa chỉ người dùng
        repository.getAddressDefaultByUserId(userId).enqueue(object : Callback<UserAddress> {
            override fun onResponse(call: Call<UserAddress>, response: Response<UserAddress>) {
                if (response.isSuccessful && response.body() != null) {
                    userAddress = response.body()
                    // 2. Lấy địa chỉ người dùng thành công, tiếp tục lấy địa chỉ admin
                    fetchAdminAddress()
                } else {
                    view?.hideLoading()
                    view?.showError("Không tìm thấy địa chỉ của bạn.")
                }
            }

            override fun onFailure(call: Call<UserAddress>, t: Throwable) {
                view?.hideLoading()
                view?.showError("Lỗi tải địa chỉ người dùng: ${t.message}")
            }
        })
    }

    /**
     * Hàm nội bộ để lấy địa chỉ admin
     */
    private fun fetchAdminAddress() {
        // 2. Lấy địa chỉ admin
        repository.getAdminAddresses().enqueue(object : Callback<UserAddress> {
            override fun onResponse(call: Call<UserAddress>, response: Response<UserAddress>) {
                if (response.isSuccessful && response.body() != null) {
                    adminAddress = response.body()
                    // 3. Cả hai địa chỉ đã có, bắt đầu tìm đường
                    triggerRouteLoad()
                } else {
                    view?.hideLoading()
                    view?.showError("Không tìm thấy địa chỉ cửa hàng (admin).")
                }
            }

            override fun onFailure(call: Call<UserAddress>, t: Throwable) {
                view?.hideLoading()
                view?.showError("Lỗi tải địa chỉ admin: ${t.message}")
            }
        })
    }

    /**
     * Hàm nội bộ để gọi hàm loadRoute gốc
     */
    private fun triggerRouteLoad() {
        // 4. Trích xuất tọa độ
        // (Giả sử UserAddress.java có getLatitude() và getLongitude())
        val startLat = userAddress?.latitude
        val startLng = userAddress?.longitude
        val endLat = adminAddress?.latitude
        val endLng = adminAddress?.longitude

        if (startLat != null && startLng != null && endLat != null && endLng != null) {
            // 4. Ra lệnh cho View vẽ marker ngay lập tức
            view?.showStartMarker(startLat, startLng) // <-- Vẽ chấm xanh
            view?.showEndMarker(endLat, endLng)     // <-- Vẽ marker đỏ
            // 5. Gọi hàm loadRoute (LiveData) với tọa độ đã lấy được
            loadRoute(startLat, startLng, endLat, endLng)
        } else {
            view?.hideLoading()
            view?.showError("Dữ liệu tọa độ không hợp lệ.")
        }
    }


    // --- HÀM CŨ (loadRoute) ---
    // Hàm này giờ chỉ chịu trách nhiệm gọi API 'getRouteAddress'
    override fun loadRoute(startLat: Double, startLng: Double, endLat: Double, endLng: Double) {
        // (Hàm này không cần gọi view?.showLoading() nữa, vì đã gọi ở trên)

        // 1. Tạo request (ĐÃ SỬA để gọi constructor Java)
        val request = RouteRequest(
            startLat,
            startLng,
            endLat,
            endLng
        )

        // Lấy LifecycleOwner từ View (Activity)
        val lifecycleOwner = view?.getLifecycleOwner() ?: return

        // Hủy observer cũ nếu có
        routeObserver?.let { obs ->
            routeLiveData?.removeObserver(obs)
        }

        // 2. Tạo observer mới
        routeObserver = Observer { response ->
            view?.hideLoading() // <--- Ẩn loading khi có kết quả cuối cùng
            if (response?.coordinates != null && response.coordinates.isNotEmpty()) {
                // 3. Nếu thành công, ra lệnh cho View vẽ
                view?.drawRoute(response.coordinates)
            } else {
                // 4. Nếu thất bại, ra lệnh cho View báo lỗi
                view?.showError("Không tìm thấy lộ trình.")
            }
        }

        // 5. Gọi repository và observe LiveData
        routeLiveData = repository.getRouteAddress(request)
        routeLiveData?.observe(lifecycleOwner, routeObserver!!)
    }

    override fun detachView() {
        routeObserver?.let { obs ->
            routeLiveData?.removeObserver(obs)
        }
        this.view = null
    }
}