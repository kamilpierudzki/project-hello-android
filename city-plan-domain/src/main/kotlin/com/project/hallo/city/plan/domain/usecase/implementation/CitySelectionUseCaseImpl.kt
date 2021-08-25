package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.usecase.CitySelectionUseCase
import com.project.hallo.commons.domain.data.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CitySelectionUseCaseImpl(
    private val cityPlanRepository: CityPlanRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CitySelectionUseCase {

    override fun execute(city: CityPlan): Flow<Response<CityPlan>> = flow {
        emit(Response.Loading())
        val cityDataResource = cityPlanRepository.getCityDataResource()
        cityDataResource.saveCurrentlySelectedCity(city)
        emit(Response.Success(city))
    }
        .flowOn(ioDispatcher)
}