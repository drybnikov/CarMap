package com.test.denis.carmap.ui

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.test.denis.carmap.viewmodel.carModel
import org.junit.jupiter.api.Test

class CarsMapPresenterTest {
    private var view: CarsMapView = mock()

    private val presenter = CarsMapPresenter()

    @Test
    fun `move camera to first car after load data`() {
        presenter.onAttach(view)

        presenter.onCarsLoaded(carsList)

        verify(view).moveCamera(1.0, 1.0, 12.0f)
    }

    @Test
    fun `show cars market in view after load data`() {
        presenter.onAttach(view)

        presenter.onCarsLoaded(carsList)

        verify(view).showCarMarkers(carsList)
    }

    @Test
    fun `move camera to selected car`() {
        presenter.onAttach(view)

        presenter.onCarSelected(LatLng(5.0, 5.0))

        verify(view).moveCamera(5.0, 5.0, 14.0f)
    }

    @Test
    fun `do nothing after detach and get some event`() {
        presenter.onAttach(view)

        presenter.onDetach()
        presenter.onCarSelected(LatLng(5.0, 5.0))

        verifyNoMoreInteractions(view)
    }

    companion object {
        private const val CAR_ID_1 = "car_0_1"
        private const val CAR_ID_2 = "car_0_2"
        private const val CAR_ID_3 = "car_0_3"

        private val carsList = listOf(
            carModel(id = CAR_ID_1, latitude = 1.0, longitude = 1.0),
            carModel(id = CAR_ID_2, latitude = 2.0, longitude = 2.0),
            carModel(id = CAR_ID_3, latitude = 3.0, longitude = 3.0)
        )
    }
}