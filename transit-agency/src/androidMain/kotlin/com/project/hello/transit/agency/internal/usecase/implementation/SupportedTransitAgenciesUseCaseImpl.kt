package com.project.hello.transit.agency.internal.usecase.implementation

import com.project.hello.commons.data.Response
import com.project.hello.commons.hilt.IoDispatcher
import com.project.hello.transit.agency.internal.usecase.SupportedTransitAgenciesUseCase
import com.project.hello.transit.agency.model.ErrorCode.SupportedCities
import com.project.hello.transit.agency.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class SupportedTransitAgenciesUseCaseImpl @Inject constructor(
    private val transitAgencyPlanRepository: TransitAgencyPlanRepository,
    private val supportedTransitAgenciesUseCaseErrorMapper: SupportedTransitAgenciesUseCaseErrorMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SupportedTransitAgenciesUseCase {

    override fun execute(): Flow<Response<SupportedTransitAgenciesData>> = flow {
        emit(Response.Loading())
        val transitAgencies = transitAgencyPlanRepository.loadTransitAgencies()
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