package com.test.denis.carmap.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.test.denis.carmap.model.CarModel
import com.test.denis.carmap.network.CarsRepository
import com.test.denis.carmap.network.Resource
import io.reactivex.subjects.BehaviorSubject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import utility.InstantExecutorExtension
import utility.RxImmediateSchedulerExtension

@ExtendWith(InstantExecutorExtension::class, RxImmediateSchedulerExtension::class)
class CarsViewModelTest {

    private val carsListSubject: BehaviorSubject<List<CarModel>> = BehaviorSubject.create()
    private val repository: CarsRepository = mock {
        whenever(it.carsList()).thenReturn(carsListSubject)
    }

    private val viewModel = CarsViewModel(repository)

    @Test
    fun `request cars list on loadCarsData call`() {
        viewModel.loadCarsData()

        verify(repository).carsList()
    }

    @Test
    fun `update progress when subscribe on loadData`() {
        viewModel.loadCarsData()

        assertTrue(viewModel.loadingProgress.value == true)
    }

    @Test
    fun `set progress value to false when data loaded`() {
        viewModel.loadCarsData()

        carsListSubject.onNext(carsList)

        assertTrue(viewModel.loadingProgress.value == false)
    }

    @Test
    fun `get Success Resource when data loaded`() {
        val liveData = viewModel.loadCarsData()

        carsListSubject.onNext(carsList)

        assertTrue(liveData.value is Resource.Success)
    }

    @Test
    fun `get Failure Resource when some error happens on data load`() {
        val liveData = viewModel.loadCarsData()

        carsListSubject.onError(Throwable("error"))

        assertTrue(liveData.value is Resource.Failure)
    }

    @Test
    fun `set progress value to false when some error happens on data load`() {
        viewModel.loadCarsData()

        carsListSubject.onError(Throwable("error"))

        assertTrue(viewModel.loadingProgress.value == false)
    }

    @Test
    fun `request cars list on retryLoad call`() {
        viewModel.retryLoad()

        verify(repository).carsList()
    }

    companion object {
        private const val CAR_ID_1 = "car_0_1"
        private const val CAR_ID_2 = "car_0_2"
        private const val CAR_ID_3 = "car_0_3"

        private val carsList = listOf(
            carModel(id = CAR_ID_1),
            carModel(id = CAR_ID_2),
            carModel(id = CAR_ID_3)
        )
    }
}