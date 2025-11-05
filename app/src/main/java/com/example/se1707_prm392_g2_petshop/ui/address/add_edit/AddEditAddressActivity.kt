package com.example.se1707_prm392_g2_petshop.ui.address.add_edit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.se1707_prm392_g2_petshop.R
import com.example.se1707_prm392_g2_petshop.databinding.ActivityAddEditAddressBinding
import com.example.se1707_prm392_g2_petshop.data.dtos.requests.UserAddressRequest
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import com.example.se1707_prm392_g2_petshop.data.utils.JwtUtil
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location

class AddEditAddressActivity : AppCompatActivity(), AddEditAddressContract.View {

    private lateinit var binding: ActivityAddEditAddressBinding
    private lateinit var presenter: AddEditAddressPresenter
    private var currentAddress: UserAddress? = null

    private var pointManager: PointAnnotationManager? = null
    private var selectedLat = 0.0
    private var selectedLon = 0.0

    // KHAI BÁO KIỂU RÕ RÀNG - tránh "Cannot infer a type"
    private val positionListener: OnIndicatorPositionChangedListener =
        OnIndicatorPositionChangedListener { point ->
            // point.latitude()/longitude() dùng được
            selectedLat = point.latitude()
            selectedLon = point.longitude()

            // nếu không muốn camera nhảy liên tục, thêm flag kiểm soát ở đây
            moveCamera(selectedLat, selectedLon)
        }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        ) {
            initLocation()
        } else {
            showMessage("Cần quyền vị trí để tiếp tục")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // =============================================
        // ✅ THÊM NÚT BACK TẠI ĐÂY
        binding.btnBack.setOnClickListener {
            finish()// Hoặc finish()
        }
        // =============================================

        presenter = AddEditAddressPresenter(
            this,
            UserAddressRepository.getInstance(this),
            this
        )

        currentAddress = intent.getSerializableExtra("address") as? UserAddress

        initMap()
        binding.btnSave.setOnClickListener { saveAddress() }
        binding.fabLocate.setOnClickListener { requestPermissionIfNeeded() }

//        if (currentAddress != null) {
//            // chế độ sửa: chỉ hiển thị marker có sẵn
//            hideLocationPuck()
//            showAddress(currentAddress!!)
//        } else {
//            // chế độ thêm: khởi tạo puck (nếu có quyền)
//            requestPermissionIfNeeded()
//        }
    }

    private fun initMap() {
        binding.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->
            pointManager = binding.mapView.annotations.createPointAnnotationManager()

            // Đăng ký image vào style và dùng id string khi tạo marker
            bitmapFromDrawableRes(this, R.drawable.red_marker)?.let { bmp ->
                // addImage method on Style expects ImageHolder; docs/examples use style.addImage(name, bitmap)
                // In v11 style.addImage(String, Bitmap) exists in extension; if not, use style.addImage(name, ImageHolder.fromBitmap(bmp))
                try {
                    style.addImage("marker_red", bmp)
                } catch (ex: Exception) {
                    // nếu không tồn tại phương thức addImage(Bitmap) ở build của bạn,
                    // ta sẽ fallback by not crashing — marker creation below will use .withIconImage("marker_red")
                }
            }

            binding.mapView.gestures.addOnMapClickListener { point ->
                // user chọn bằng click -> tắt puck và đặt marker
                hideLocationPuck()
                selectedLat = point.latitude()
                selectedLon = point.longitude()
                moveCamera(selectedLat, selectedLon)
                drawMarker(selectedLat, selectedLon)
                true
            }

            if (currentAddress != null) {
                // chế độ sửa: chỉ hiển thị marker có sẵn
                hideLocationPuck()
                showAddress(currentAddress!!)
            } else {
                // chế độ thêm: khởi tạo puck (nếu có quyền)
                requestPermissionIfNeeded()
            }
        }
    }

    private fun requestPermissionIfNeeded() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED) {
            initLocation()
        } else {
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    private fun initLocation() {
        pointManager?.deleteAll()
        val lc = binding.mapView.location
        lc.updateSettings {
            enabled = true
            pulsingEnabled = true
        }
        // DÙNG đúng tên hàm của v11:
        lc.addOnIndicatorPositionChangedListener(positionListener)
    }

    private fun hideLocationPuck() {
        val lc = binding.mapView.location
        lc.updateSettings { enabled = false }
        // DÙNG đúng tên hàm của v11:
        lc.removeOnIndicatorPositionChangedListener(positionListener)
    }

    private fun moveCamera(lat: Double, lon: Double) {
        binding.mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(lon, lat))
                .zoom(16.0)
                .build()
        )
    }

    private fun drawMarker(lat: Double, lon: Double) {
        pointManager?.deleteAll()
        val opt = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(lon, lat))
            // DÙNG id hình đã thêm vào style: "marker_red"
            .withIconImage("marker_red")
        pointManager?.create(opt)
    }

    private fun saveAddress() {
        val userId = JwtUtil.getSubFromToken(this)?.toIntOrNull() ?: run {
            showMessage("Không lấy được userId")
            return
        }

        val req = UserAddressRequest(
            userId,
            binding.etAddressLine.text.toString(),
            binding.etCity.text.toString(),
            binding.etDistrict.text.toString(),
            binding.etWard.text.toString(),
            "",
            binding.switchDefault.isChecked,
            selectedLat,
            selectedLon
        )

        presenter.saveAddress(currentAddress, req)
    }

    override fun showAddress(address: UserAddress) {
        binding.etAddressLine.setText(address.addressLine)
        binding.etCity.setText(address.city)
        binding.etDistrict.setText(address.district)
        binding.etWard.setText(address.ward)
        binding.switchDefault.isChecked = address.isDefault

        updateMap(address.latitude, address.longitude)
    }

    // ensure contract requires this method; implement override
    override fun updateMap(lat: Double, lon: Double) {
        moveCamera(lat, lon)
        drawMarker(lat, lon)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishView() {
        setResult(RESULT_OK)
        finish()
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resId: Int): Bitmap? {
        val d = AppCompatResources.getDrawable(context, resId) ?: return null
        return if (d is BitmapDrawable) d.bitmap else run {
            val bm = Bitmap.createBitmap(d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val c = Canvas(bm)
            d.setBounds(0, 0, c.width, c.height)
            d.draw(c)
            bm
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            binding.mapView.location.removeOnIndicatorPositionChangedListener(positionListener)
        } catch (e: Exception) {
            // ignore if not registered
        }
    }
}
