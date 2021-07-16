package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.api.toCityPlan
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCase
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCaseErrorMapper
import com.project.hallo.commons.domain.repository.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SelectedCityUseCaseImpl(
    private val cityPlanRepository: CityPlanRepository,
    private val selectedCityUseCaseErrorMapper: SelectedCityUseCaseErrorMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SelectedCityUseCase {

    override fun execute(): Flow<Response<CityPlan>> = flow {
        emit(Response.Loading())
        val cityDataResource = cityPlanRepository.getCityDataResource()
        val selectedCityResponse: Response<CityPlan> = cityDataResource.getCurrentlySelectedCity()
        if (selectedCityResponse is Response.Error) {
            selectedCityUseCaseErrorMapper.mapError(selectedCityResponse)
            emit(selectedCityResponse)
        } else {
            val updatedSelectedCity: CityPlan? = cityPlanRepository.getSupportedCityFileResources()
                .map { supportedCityFile -> cityDataResource.load(supportedCityFile) }
                .mapNotNull { cityPlanResponse -> cityPlanResponse.data }
                .map { it.toCityPlan() }
                .firstOrNull {
                    it.city == selectedCityResponse.data?.city
                }
            if (updatedSelectedCity != null) {
                emit(Response.Success(updatedSelectedCity))
            } else {
                emit(selectedCityResponse)
            }
        }
    }
        .flowOn(ioDispatcher)
}