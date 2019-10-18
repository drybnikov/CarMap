package com.test.denis.carmap.network

import com.test.denis.carmap.model.CarModel
import com.test.denis.carmap.model.toModel
import io.reactivex.Observable
import javax.inject.Inject

class CarsRepository @Inject constructor(private val api: CarsRepositoryApi) {

    fun carsList(): Observable<List<CarModel>> =
        api.getCarsList().map { list ->
            list.map { it.toModel() }
        }
}