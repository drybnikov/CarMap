package com.test.denis.carmap.network

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.test.denis.carmap.viewmodel.carResponse
import io.reactivex.subjects.BehaviorSubject
import org.junit.jupiter.api.Test

class CarsRepositoryTest {

    private val carsListSubject: BehaviorSubject<List<CarResponse>> = BehaviorSubject.create()
    private val api: CarsRepositoryApi = mock {
        whenever(it.getCarsList()).thenReturn(carsListSubject)
    }

    private val repository = CarsRepository(api)

    @Test
    fun `call api to get cars list`() {
        repository.carsList()

        verify(api).getCarsList()
    }

    @Test
    fun `map response object to model`() {
        val testObservable = repository.carsList().test()

        carsListSubject.onNext(carsList)

        testObservable.assertOf {
            val carModel = it.values().first().first()
            assert(carModel.id == CAR_ID_1)
            assert(carModel.imageUrl == "")
        }
    }

    companion object {
        private const val CAR_ID_1 = "car_0_1"
        private const val CAR_ID_2 = "car_0_2"
        private const val CAR_ID_3 = "car_0_3"

        private val carsList = listOf(
            carResponse(id = CAR_ID_1),
            carResponse(id = CAR_ID_2),
            carResponse(id = CAR_ID_3)
        )
    }
}