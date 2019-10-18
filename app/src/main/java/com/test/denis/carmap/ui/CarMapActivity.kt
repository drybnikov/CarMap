package com.test.denis.carmap.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.test.denis.carmap.R
import com.test.denis.carmap.di.AbstractViewModelFactory
import com.test.denis.carmap.di.Injectable
import com.test.denis.carmap.model.CarModel
import com.test.denis.carmap.network.Resource
import com.test.denis.carmap.util.setVisibility
import com.test.denis.carmap.viewmodel.CarsViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_car_map.*
import javax.inject.Inject

class CarMapActivity : AppCompatActivity(), Injectable, HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: AbstractViewModelFactory<CarsViewModel>

    private var locationPermissionGranted: Boolean = false
    private lateinit var viewModel: CarsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_map)

        initViewModel()

        if (savedInstanceState == null) {
            getDeviceLocation()
        }

        carListLayout.setItemSelectedAction { onListItemSelected(it) }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(CarsViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadCarsData().observe(this, Observer { onDataLoaded(it) })

        viewModel.loadingProgress.observe(this, Observer { toggleLoadingIndicatorVisibility(it) })
    }

    private fun onDataLoaded(resource: Resource<List<CarModel>>) {
        when (resource) {
            is Resource.Success -> carListLayout.setData(resource.data)
            is Resource.Failure -> showError(resource.throwable.localizedMessage)
        }
    }

    private fun showError(errorMessage: String) {
        Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction(R.string.retry) { viewModel.retryLoad() }
                this.dismiss()
            }
    }

    private fun onListItemSelected(carModel: CarModel) {
        viewModel.onCarSelected(carModel)
    }

    private fun toggleLoadingIndicatorVisibility(isVisible: Boolean) {
        loadingProgress.setVisibility(isVisible)
    }

    private fun openMap() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, CarsMapFragment())
            .commit()
    }

    private fun getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }

        if (locationPermissionGranted) {
            openMap()
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        openMap()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12564
    }
}