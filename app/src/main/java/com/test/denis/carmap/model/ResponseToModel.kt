package com.test.denis.carmap.model

import com.test.denis.carmap.network.CarResponse

internal fun CarResponse.toModel() = CarModel(
    id = id,
    modelName = modelName,
    name = name,
    make = make,
    group = group,
    color = color,
    series = series,
    fuelType = fuelType,
    fuelLevel = fuelLevel,
    transmission = transmission,
    licensePlate = licensePlate,
    latitude = latitude,
    longitude = longitude,
    innerCleanliness = innerCleanliness,
    imageUrl = carImageUrl
)