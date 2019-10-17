package com.test.denis.carmap.ui

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class CarsMapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

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
    }
}