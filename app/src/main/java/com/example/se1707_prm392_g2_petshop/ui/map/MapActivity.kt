package com.example.se1707_prm392_g2_petshop.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources

import com.example.se1707_prm392_g2_petshop.data.models.UserAddress
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import com.example.se1707_prm392_g2_petshop.data.retrofit.RetrofitClient
import com.example.se1707_prm392_g2_petshop.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


class MapActivity : AppCompatActivity(), MapContract.View {

    private lateinit var mapView: MapView
    private lateinit var btnBack: ImageButton
    private lateinit var presenter: MapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Ánh xạ view
        mapView = findViewById(R.id.mapView)
        btnBack = findViewById(R.id.btnBack)

        // Nút back → quay lại HomeFragment
        btnBack.setOnClickListener { finish() }

        // Load bản đồ
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

        // Gọi API địa chỉ
        val api = RetrofitClient.getUserAddressApi(this)
        val repository = UserAddressRepository(this)
        presenter = MapPresenter(this, repository)
        presenter.loadDefaultAddress(1)
    }

    override fun showAddress(address: UserAddress) {
        if (address.latitude == 0.0 || address.longitude == 0.0) {
            showError("Địa chỉ chưa có tọa độ hợp lệ")
            return
        }

        val point = Point.fromLngLat(address.longitude, address.latitude)
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(point)
                .zoom(15.0)
                .build()
        )

        addRedMarker(point)
    }

    private fun addRedMarker(point: Point) {
        val bitmap = bitmapFromDrawableRes(this, R.drawable.red_marker) ?: return
        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconSize(1.0)
        pointAnnotationManager.create(pointAnnotationOptions)
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

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}


