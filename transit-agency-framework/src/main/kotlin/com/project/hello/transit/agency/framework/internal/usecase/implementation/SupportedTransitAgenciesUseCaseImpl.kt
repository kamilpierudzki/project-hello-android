package com.project.hello.transit.agency.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.hilt.IoDispatcher
import com.project.hello.transit.agency.domain.model.ErrorCode.SupportedCities
import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.domain.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import com.project.hello.transit.agency.framework.internal.model.api.toTransitAgency
import com.project.hello.transit.agency.framework.internal.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.framework.internal.usecase.SupportedTransitAgenciesUseCase
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

        val transitAgencies =
            transitAgencyPlanRepository.getSupportedTransitAgenciesFileResources()
                .map { transitAgencyDataResource.loadTransitAgency(it) }
                .mapNotNull { (it as? ResponseApi.Success)?.successData }
                .map { it.toTransitAgency() }

        if (transitAgencies.isEmpty()) {
            val errorResponse = Response.Error<SupportedTransitAgenciesData>(
                SupportedCities.SUPPORTED_CITIES_ERROR
            )
            supportedTransitAgenciesUseCaseErrorMapper.mapError(errorResponse)
            emit(errorResponse)
        } else {
            emit(Response.Success(SupportedTransitAgenciesData(transitAgencies)))
        }
    }
        .flowOn(ioDispatcher)
}