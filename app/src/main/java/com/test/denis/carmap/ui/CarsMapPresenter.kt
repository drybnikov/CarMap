package com.test.denis.carmap.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.denis.carmap.model.CarModel
import javax.inject.Inject

class CarsMapPresenter @Inject constructor() {

    private var view: CarsMapView? = null

    fun onAttach(view: CarsMapView) {
        this.view = view
    }

    fun onCarsLoaded(carItems: List<CarModel>) {
        val firstCar = carItems.first()

        view?.moveCamera(firstCar.latitude, firstCar.longitude, DEFAULT_ZOOM)

        view?.showCarMarkers(carItems)
    }

    fun onCarImageLoaded(model: CarModel, targetWidth: Int): CustomTarget<Bitmap> {
        return object : CustomTarget<Bitmap>(targetWidth, targetWidth) {
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

        view?.addMarker(markerOptions)
    }

    fun onCarSelected(position: LatLng) {
        view?.moveCamera(position.latitude, position.longitude, CAR_ZOOM)
    }

    fun onDetach() {
        view = null
    }

    companion object {
        private const val DEFAULT_ZOOM = 12.0f
        private const val CAR_ZOOM = 14.0f
    }
}

interface CarsMapView {
    fun moveCamera(latitude: Double, longitude: Double, zoom: Float)
    fun addMarker(markerOptions: MarkerOptions)
    fun showCarMarkers(carItems: List<CarModel>)
}