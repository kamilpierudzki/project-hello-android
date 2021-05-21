package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCase
import com.project.hallo.commons.domain.repository.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SelectedCityUseCaseImpl(
    private val cityPlanRepository: CityPlanRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SelectedCityUseCase {

    override fun execute(): Flow<Response<CityPlan>> = flow {
        emit(Response.Loading())
        val cityDataResource = cityPlanRepository.getCityDataResource()
        val response: Response<CityPlan> = cityDataResource.getCurrentlySelectedCity()
        emit(response)
    }
        .flowOn(ioDispatcher)
}