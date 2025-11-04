package com.example.se1707_prm392_g2_petshop.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LifecycleOwner
import com.example.se1707_prm392_g2_petshop.R
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


class MapActivity : AppCompatActivity(), MapContract.View {

    private lateinit var mapView: MapView
    private lateinit var loadingProgressBar: ProgressBar

    private lateinit var mapboxMap: MapboxMap
    private lateinit var presenter: MapContract.Presenter

    private var pointManager: PointAnnotationManager? = null

    // Định danh cho source và layer trên bản đồ
    private val ROUTE_SOURCE_ID = "route-source-id"
    private val ROUTE_LAYER_ID = "route-layer-id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map) // File layout XML v11

        // ... (ánh xạ view và khởi tạo presenter như cũ) ...
        mapView = findViewById(R.id.mapView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        mapboxMap = mapView.getMapboxMap()

        val annotationPlugin: AnnotationPlugin = mapView.annotations
        pointManager = annotationPlugin.createPointAnnotationManager()

        val repository = UserAddressRepository.getInstance(this.applicationContext)
        presenter = MapPresenter(repository)
        presenter.attachView(this)

        // Tải style và yêu cầu Presenter tải route
        loadMapAndRoute()
    }

    private fun loadMapAndRoute() {
        val userIdString = JwtUtil.getSubFromToken(this)
        if (userIdString.isNullOrEmpty()) {
            Toast.makeText(this, "Không tìm thấy User ID trong token", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = userIdString.toIntOrNull()
        if (userId == null) {
            Toast.makeText(this, "User ID không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        mapboxMap.loadStyle(
            style(Style.MAPBOX_STREETS) {
                // Style đã tải xong

                // --- THAY ĐỔI Ở ĐÂY ---
                // Thay vì gọi presenter.loadRoute(...) với tọa độ cứng,
                // hãy gọi hàm mới để bắt đầu chuỗi API

                presenter.loadRouteFromUserToAdmin(userId)
            }
        )
    }


    // --- Triển khai các hàm của MapContract.View ---

    override fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this // Activity chính là một LifecycleOwner
    }

    override fun drawRoute(routeCoordinates: List<List<Double>>) {
        // 1. Chuyển đổi List<List<Double>> (Lng, Lat) thành List<Point>
        // API của bạn trả về [Lng, Lat] nên it[0] là Lng, it[1] là Lat
        val points = routeCoordinates.map { Point.fromLngLat(it[0], it[1]) }

        // 2. Tạo một GeoJSON LineString từ danh sách điểm
        val lineString = LineString.fromLngLats(points) // <-- Sửa thành fromLngLats
        val routeFeature = Feature.fromGeometry(lineString)

        // 3. Lấy style hiện tại (đảm bảo nó không null)
        mapboxMap.getStyle()?.let { style ->

            // 4. Lấy source (nguồn dữ liệu) trên bản đồ.
            var source = style.getSource(ROUTE_SOURCE_ID) as? GeoJsonSource

            if (source == null) {
                // Nếu chưa có, tạo source mới
                source = geoJsonSource(ROUTE_SOURCE_ID) {
                    feature(routeFeature)
                }
                style.addSource(source)
            } else {
                // Nếu có rồi, cập nhật dữ liệu
                source.feature(routeFeature)
            }

            // 5. Lấy layer (lớp hiển thị)
            if (style.getLayer(ROUTE_LAYER_ID) == null) {
                // Nếu chưa có, tạo layer mới
                val routeLayer = lineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID) {
                    lineColor(Color.parseColor("#007cbf"))
                    lineWidth(5.0)
                    lineCap(LineCap.ROUND)
                    lineJoin(LineJoin.ROUND)
                }
                style.addLayer(routeLayer)
            }

            // 6. (Tùy chọn) Di chuyển camera để thấy toàn bộ route
            zoomToRoute(points)
        }
    }

    private fun zoomToRoute(points: List<Point>) {
        if (points.isEmpty()) return

        // API camera mới của v11
        val cameraOptions = mapboxMap.cameraForCoordinates(
            points,
            // Thêm padding cho camera
            com.mapbox.maps.EdgeInsets(50.0, 50.0, 50.0, 50.0),
            0.0, // Bearing
            0.0  // Pitch
        )

        // Bay đến vị trí mới
        mapboxMap.flyTo(
            cameraOptions,
            MapAnimationOptions.Builder().duration(2000).build() // 2 giây
        )
    }

    override fun clearMapMarkers() {
        pointManager?.deleteAll()
    }

    override fun showStartMarker(lat: Double, lng: Double) {
        val point = Point.fromLngLat(lng, lat)

        // Tạo bitmap marker xanh
        // !! BẠN CẦN TẠO FILE `blue_marker.png` (hoặc xml) TRONG `res/drawable`
        val bitmap = bitmapFromDrawableRes(this, R.drawable.grey_dot_marker) ?: return

        // Thêm marker mới
        val options = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconSize(1.0) // Kích thước (bạn có thể điều chỉnh)
        pointManager?.create(options)
    }

    override fun showEndMarker(lat: Double, lng: Double) {
        val point = Point.fromLngLat(lng, lat)

        // Tạo bitmap marker đỏ (sử dụng R.drawable.red_marker của bạn)
        val bitmap = bitmapFromDrawableRes(this, R.drawable.red_marker) ?: return

        // Thêm marker mới
        val options = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconSize(1.0) // Kích thước (bạn có thể điều chỉnh)
        pointManager?.create(options)
    }

    // --- Quản lý vòng đời ---
    // Không cần onStart, onResume, v.v. cho MapView trong v11

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView() // Báo cho Presenter biết View đã bị hủy
        // Không cần mapView.onDestroy() nữa
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int): Bitmap? {
        val drawable = AppCompatResources.getDrawable(context, resourceId) ?: return null
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}


