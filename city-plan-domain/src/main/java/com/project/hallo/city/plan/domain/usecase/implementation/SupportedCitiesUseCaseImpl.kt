package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.ErrorCode.SupportedCities
import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.city.plan.domain.model.api.toCityPlan
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCase
import com.project.hallo.city.plan.domain.usecase.SupportedCitiesUseCaseErrorMapper
import com.project.hallo.commons.domain.repository.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SupportedCitiesUseCaseImpl(
    private val cityPlanRepository: CityPlanRepository,
    private val supportedCitiesUseCaseErrorMapper: SupportedCitiesUseCaseErrorMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SupportedCitiesUseCase {

    override fun execute(): Flow<Response<SupportedCitiesData>> = flow {
        emit(Response.Loading())
        val cityDataResource = cityPlanRepository.getCityDataResource()
        val supportedCities: List<CityPlan> = cityPlanRepository.getSupportedCityFileResources()
            .map { supportedCityFile -> cityDataResource.load(supportedCityFile) }
            .mapNotNull { cityPlanResponse -> (cityPlanResponse as? Response.Success)?.successData }
            .map { it.toCityPlan() }

        if (supportedCities.isEmpty()) {
            val errorResponse = Response.Error<SupportedCitiesData>(
                SupportedCities.SUPPORTED_CITIES_ERROR
            )
            supportedCitiesUseCaseErrorMapper.mapError(errorResponse)
            emit(errorResponse)
        } else {
            emit(Response.Success(SupportedCitiesData(supportedCities)))
        }
    }
        .flowOn(ioDispatcher)
}