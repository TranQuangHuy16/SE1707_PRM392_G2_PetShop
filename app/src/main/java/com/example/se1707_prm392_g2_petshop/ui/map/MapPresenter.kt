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
import kotlin.math.roundToInt

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



    override fun loadRoute(startLat: Double, startLng: Double, endLat: Double, endLng: Double) {
        val request = RouteRequest(
            startLat,
            startLng,
            endLat,
            endLng
        )
        val lifecycleOwner = view?.getLifecycleOwner() ?: return
        routeObserver?.let { obs ->
            routeLiveData?.removeObserver(obs)
        }

        // <-- THÊM MỚI: Ẩn thông tin cũ (nếu có)
        view?.hideRouteInfo()

        // Tạo observer mới
        // ======================== PHẦN SỬA LỖI ========================
        routeObserver = Observer { response: RouteResponse? ->
            view?.hideLoading()

            // [FIX 2] KIỂM TRA NULL TRƯỚC TIÊN
            if (response != null && response.coordinates != null && response.coordinates.isNotEmpty()) {

                // --- Khối xử lý THÀNH CÔNG ---
                // 'response' không null VÀ 'coordinates' cũng có dữ liệu
                view?.drawRoute(response.coordinates)

                // <-- THÊM MỚI: Kiểm tra và hiển thị thông tin
                val distance = response.distance
                val duration = response.duration
                if (distance != null && duration != null) {
                    val formattedDistance = formatDistance(distance)
                    val formattedDuration = formatDuration(duration)
                    view?.showRouteInfo(formattedDistance, formattedDuration)
                }

            } else {
                // --- Khối xử lý THẤT BẠI ---
                // 'response' là null (do lỗi 500, 404, hoặc mất mạng)
                // HOẶC 'response.coordinates' là rỗng (API trả về nhưng không có đường đi)

                // (Tùy chọn) Bạn có thể kiểm tra cụ thể hơn:
                if (response == null) {
                    view?.showError("Lỗi: Không thể tìm đường. (Tuyến đường có thể quá dài hoặc có lỗi mạng)")
                } else {
                    // API chạy thành công nhưng không trả về tọa độ
                    view?.showError("Không tìm thấy lộ trình hợp lệ.")
                }
            }
        }


        // 5. Gọi repository và observe LiveData
        routeLiveData = repository.getRouteAddress(request)
        routeLiveData?.observe(lifecycleOwner, routeObserver!!)
    }
    private fun formatDistance(meters: Double): String {
        return if (meters >= 1000) {
            val kilometers = meters / 1000
            // Sử dụng "%.1f" để lấy 1 số sau dấu phẩy
            String.format("%.1f km", kilometers)
        } else {
            String.format("%.0f m", meters)
        }
    }

    /**
     * Chuyển đổi giây sang phút hoặc giờ
     * (vd: 1520.0 -> "25 phút")
     */
    private fun formatDuration(seconds: Double): String {
        val totalMinutes = (seconds / 60).roundToInt()

        return if (totalMinutes < 60) {
            "$totalMinutes phút"
        } else {
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60
            "$hours giờ $minutes phút"
        }
    }


    override fun detachView() {
        routeObserver?.let { obs ->
            routeLiveData?.removeObserver(obs)
        }
        this.view = null
    }
}