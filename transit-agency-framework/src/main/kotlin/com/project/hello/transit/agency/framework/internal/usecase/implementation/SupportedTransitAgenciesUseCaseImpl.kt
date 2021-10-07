package com.project.hello.transit.agency.framework.internal.usecase.implementation

import com.project.hello.transit.agency.domain.model.ErrorCode.SupportedCities
import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.framework.internal.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.domain.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import com.project.hello.transit.agency.framework.internal.usecase.SupportedTransitAgenciesUseCase
import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.hilt.IoDispatcher
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyStopAPI
import com.project.hello.transit.agency.framework.internal.model.api.toTransitAgency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class SupportedTransitAgenciesUseCaseImpl @Inject constructor(
    private val transitAgencyPlanRepository: TransitAgencyPlanRepository,
    private val supportedTransitAgenciesUseCaseErrorMapper: SupportedTransitAgenciesUseCaseErrorMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SupportedTransitAgenciesUseCase {

    override fun execute(): Flow<Response<SupportedTransitAgenciesData>> = flow {
        emit(Response.Loading())
        val transitAgencyDataResource = transitAgencyPlanRepository.getTransitAgencyDataResource()

        val transitAgencies: List<TransitAgencyAPI> =
            transitAgencyPlanRepository.getSupportedTransitAgenciesFileResources()
                .map { transitAgencyDataResource.loadTransitAgency(it) }
                .mapNotNull { (it as? ResponseApi.Success)?.successData }

        val transitAgencyStops: List<TransitAgencyStopAPI> =
            transitAgencyPlanRepository.getSupportedTransitAgencyStopsFileResources()
                .map { transitAgencyDataResource.loadTransitAgencyStop(it) }
                .mapNotNull { (it as? ResponseApi.Success)?.successData }

        val supportedCities: List<TransitAgency> = transitAgencies
            .zip(transitAgencyStops) { t1, t2 -> t1 to t2 }
            .map {
                it.first.toTransitAgency(it.second)
            }

        if (supportedCities.isEmpty()) {
            val errorResponse = Response.Error<SupportedTransitAgenciesData>(
                SupportedCities.SUPPORTED_CITIES_ERROR
            )
            supportedTransitAgenciesUseCaseErrorMapper.mapError(errorResponse)
            emit(errorResponse)
        } else {
            emit(Response.Success(SupportedTransitAgenciesData(supportedCities)))
        }
    }
        .flowOn(ioDispatcher)
}