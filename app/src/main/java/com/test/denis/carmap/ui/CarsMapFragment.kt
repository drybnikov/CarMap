package com.test.denis.carmap.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.denis.carmap.R
import com.test.denis.carmap.di.AbstractViewModelFactory
import com.test.denis.carmap.di.Injectable
import com.test.denis.carmap.model.CarModel
import com.test.denis.carmap.network.Resource
import com.test.denis.carmap.viewmodel.CarsViewModel
import javax.inject.Inject


class CarsMapFragment : SupportMapFragment(), Injectable, OnMapReadyCallback, CarsMapView {

    @Inject
    lateinit var factory: AbstractViewModelFactory<CarsViewModel>

    @Inject
    lateinit var presenter: CarsMapPresenter

    private lateinit var map: GoogleMap
    private lateinit var viewModel: CarsViewModel

    private val markerIconWidth by lazy {
        resources.getDimensionPixelSize(R.dimen.marker_width)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            viewModel = ViewModelProviders.of(it, factory).get(CarsViewModel::class.java)
        }

        presenter.onAttach(this)

        getMapAsync(this)
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

        viewModel.onCarSelectedData.observe(this, Observer { presenter.onCarSelected(it) })
    }

    private fun onDataLoaded(resource: Resource<List<CarModel>>) {
        when (resource) {
            is Resource.Success -> presenter.onCarsLoaded(resource.data)
        }
    }

    override fun moveCamera(latitude: Double, longitude: Double, zoom: Float) {
        val carLatLng = LatLng(latitude, longitude)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(carLatLng, zoom))
    }

    override fun showCarMarkers(carItems: List<CarModel>) {
        val context = context ?: return
        val glide = Glide.with(context)

        carItems.forEach {
            glide
                .asBitmap()
                .load(it.imageUrl)
                .override(markerIconWidth, markerIconWidth)
                .into(presenter.onCarImageLoaded(it, markerIconWidth))
        }
    }

    override fun addMarker(markerOptions: MarkerOptions) {
        map.addMarker(markerOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }
}