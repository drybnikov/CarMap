package com.test.denis.carmap.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.denis.carmap.R
import com.test.denis.carmap.di.AbstractViewModelFactory
import com.test.denis.carmap.di.Injectable
import com.test.denis.carmap.model.CarModel
import com.test.denis.carmap.network.Resource
import com.test.denis.carmap.viewmodel.CarsViewModel
import javax.inject.Inject


class CarsMapFragment : SupportMapFragment(), Injectable, OnMapReadyCallback {

    @Inject
    lateinit var factory: AbstractViewModelFactory<CarsViewModel>

    private lateinit var map: GoogleMap
    private lateinit var viewModel: CarsViewModel

    private val markerIconWidth by lazy {
        resources.getDimensionPixelSize(R.dimen.marker_width)
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            viewModel = ViewModelProviders.of(it, factory).get(CarsViewModel::class.java)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.apply {
            isMyLocationEnabled = true
            isIndoorEnabled = true
            isTrafficEnabled = true
            uiSettings.isZoomControlsEnabled = true
        }

        viewModel.loadCarsData().observe(this, Observer { onDataLoaded(it) })

        viewModel.onCarSelectedData.observe(this, Observer { moveCameraToSelectedCar(it) })
    }

    private fun onDataLoaded(resource: Resource<List<CarModel>>) {
        when (resource) {
            is Resource.Success -> showCarsOnMap(resource.data)
        }
    }

    private fun moveCameraToSelectedCar(position: LatLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, CAR_ZOOM))
    }

    @Suppress("DEPRECATION")
    private fun showCarsOnMap(carItems: List<CarModel>) {
        val firstCar = carItems.first()
        val carLatLng = LatLng(firstCar.latitude, firstCar.longitude)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(carLatLng, DEFAULT_ZOOM))

        addCarMarkers(carItems)
    }

    private fun addCarMarkers(carItems: List<CarModel>) {
        val context = context ?: return
        val glide = Glide.with(context)

        carItems.forEach {
            glide
                .asBitmap()
                .load(it.imageUrl)
                .override(markerIconWidth, markerIconWidth)
                .into(customGlideTarget(it))
        }
    }

    private fun customGlideTarget(model: CarModel): CustomTarget<Bitmap> {
        return object : CustomTarget<Bitmap>(markerIconWidth, markerIconWidth) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                addMarker(resource, model)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                addMarker(null, model)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                addMarker(null, model)
            }
        }
    }

    private fun addMarker(resource: Bitmap?, model: CarModel) {
        val carLatLng = LatLng(model.latitude, model.longitude)

        val markerOptions = MarkerOptions()
            .position(carLatLng)
            .title("${model.modelName} ${model.name}")
            .snippet(model.licensePlate)

        if (resource != null) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resource)).flat(false)
        }

        map.addMarker(markerOptions)
    }

    companion object {
        private const val DEFAULT_ZOOM = 12.0f
        private const val CAR_ZOOM = 14.0f
    }
}