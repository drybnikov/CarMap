package com.test.denis.carmap.network

import io.reactivex.Observable
import retrofit2.http.GET

const val BASE_URL: String = "http://cdn.sixt.io/"

interface CarsRepositoryApi {
    @GET("codingtask/cars")
    fun getCarsList(): Observable<List<CarResponse>>
}