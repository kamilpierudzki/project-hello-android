package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.api.toCityPlan
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCase
import com.project.hallo.city.plan.domain.usecase.SelectedCityUseCaseErrorMapper
import com.project.hallo.commons.domain.data.Response
import com.project.hallo.commons.domain.data.ResponseApi
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
        val selectedCityResponseApi: ResponseApi<CityPlan> =
            cityDataResource.getCurrentlySelectedCity()
        when (selectedCityResponseApi) {
            is ResponseApi.Error -> {
                val selectedCityResponse =
                    Response.Error<CityPlan>(selectedCityResponseApi.rawErrorMessage)
                        .also {
                            selectedCityUseCaseErrorMapper.mapError(it)
                        }
                emit(selectedCityResponse)
            }
            is ResponseApi.Success -> {
                val updatedSelectedCity: CityPlan? =
                    cityPlanRepository.getSupportedCityFileResources()
                        .map { supportedCityFile -> cityDataResource.load(supportedCityFile) }
                        .mapNotNull {
                            it.data
                        }
                        .map { it.toCityPlan() }
                        .firstOrNull {
                            it.city == selectedCityResponseApi.successData.city
                        }
                if (updatedSelectedCity != null) {
                    emit(Response.Success(updatedSelectedCity))
                } else {
                    emit(Response.Success(selectedCityResponseApi.successData))
                }
            }
        }
    }
        .flowOn(ioDispatcher)
}