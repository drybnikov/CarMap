package com.test.denis.carmap.viewmodel

import com.test.denis.carmap.model.CarModel
import com.test.denis.carmap.network.CarResponse

fun carModel(id: String = "", name: String = "", latitude: Double = 0.0, longitude: Double = 0.0) =
    CarModel(
        id = id,
        modelName = "",
        name = name,
        make = "",
        group = "",
        color = "",
        series = "",
        fuelType = "",
        fuelLevel = 0f,
        transmission = "",
        licensePlate = "",
        latitude = latitude,
        longitude = longitude,
        innerCleanliness = "",
        imageUrl = ""
    )

fun carResponse(
    id: String = "",
    name: String = "",
    latitude: Double = 0.0,
    longitude: Double = 0.0
) =
    CarResponse(
        id = id,
        modelName = "",
        name = name,
        make = "",
        group = "",
        color = "",
        series = "",
        fuelType = "",
        fuelLevel = 0f,
        transmission = "",
        licensePlate = "",
        latitude = latitude,
        longitude = longitude,
        innerCleanliness = "",
        modelIdentifier = "",
        carImageUrl = ""
    )