package com.example.se1707_prm392_g2_petshop.ui.map

import androidx.lifecycle.LifecycleOwner
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress

interface MapContract {

    interface View {
        // ... (các hàm cũ) ...
        fun showLoading()
        fun hideLoading()
        fun drawRoute(routeCoordinates: List<List<Double>>)
        fun showError(message: String)
        fun getLifecycleOwner(): LifecycleOwner

        // --- CÁC HÀM MỚI ---
        /** Xóa tất cả marker khỏi bản đồ */
        fun clearMapMarkers()

        /** * Vẽ marker cho điểm bắt đầu (vị trí user)
         * @param lat Vĩ độ
         * @param lng Kinh độ
         */
        fun showStartMarker(lat: Double, lng: Double)

        /** * Vẽ marker cho điểm kết thúc (vị trí admin)
         * @param lat Vĩ độ
         * @param lng Kinh độ
         */
        fun showEndMarker(lat: Double, lng: Double)
    }

    interface Presenter {
        // ... (các hàm cũ) ...
        fun attachView(view: View)
        fun detachView()
        fun loadRoute(startLat: Double, startLng: Double, endLat: Double, endLng: Double)
        fun loadRouteFromUserToAdmin(userId: Int)
    }
}