package com.test.denis.carmap.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.test.denis.carmap.model.CarModel
import com.test.denis.carmap.network.CarsRepository
import com.test.denis.carmap.network.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class CarsViewModel @Inject constructor(private val repository: CarsRepository) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val data = MutableLiveData<Resource<List<CarModel>>>()
    private val _carSelectionData = MutableLiveData<LatLng>()
    val onCarSelectedData: LiveData<LatLng> = _carSelectionData

    val loadingProgress = MutableLiveData<Boolean>(false)

    fun loadCarsData(): LiveData<Resource<List<CarModel>>> {
        if (data.value == null) {
            loadData()
        }

        return data
    }

    private fun loadData() {
        repository
            .carsList()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingProgress.value = true }
            .subscribe({ onSuccess(it) }, this::onError)
            .addTo(disposables)
    }

    private fun onSuccess(newModelList: List<CarModel>) {
        data.value = Resource.Success(newModelList)

        loadingProgress.value = false
    }

    private fun onError(error: Throwable) {
        Log.w("CarsViewModel", "onError", error)

        data.value = Resource.Failure(error)
        loadingProgress.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun retryLoad() {
        if (loadingProgress.value == false) {
            loadData()
        }
    }

    fun onCarSelected(model: CarModel) {
        _carSelectionData.postValue(LatLng(model.latitude, model.longitude))
    }
}