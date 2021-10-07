package com.project.hello.transit.agency.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.hilt.IoDispatcher
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.domain.usecase.SelectedTransitAgencyUseCaseErrorMapper
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyStopAPI
import com.project.hello.transit.agency.framework.internal.model.api.toTransitAgency
import com.project.hello.transit.agency.framework.internal.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.framework.internal.usecase.SelectedTransitAgencyUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class SelectedTransitAgencyUseCaseImpl @Inject constructor(
    private val transitAgencyPlanRepository: TransitAgencyPlanRepository,
    private val selectedTransitAgencyUseCaseErrorMapper: SelectedTransitAgencyUseCaseErrorMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SelectedTransitAgencyUseCase {

    override fun execute(): Flow<Response<TransitAgency>> = flow {
        emit(Response.Loading())
        val transitAgencyDataResource = transitAgencyPlanRepository.getTransitAgencyDataResource()
        when (val selectedTransitAgencyResponseApi =
            transitAgencyDataResource.getCurrentlySelectedTransitAgency()) {
            is ResponseApi.Error -> {
                val selectedCityResponse =
                    Response.Error<TransitAgency>(selectedTransitAgencyResponseApi.rawErrorMessage)
                        .also {
                            selectedTransitAgencyUseCaseErrorMapper.mapError(it)
                        }
                emit(selectedCityResponse)
            }
            is ResponseApi.Success -> {
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
                    .map { it.first.toTransitAgency(it.second) }

                val updatedSelectedTransitAgency: TransitAgency? = supportedCities
                    .firstOrNull {
                        it.transitAgency == selectedTransitAgencyResponseApi.successData.transitAgency
                    }
                if (updatedSelectedTransitAgency != null) {
                    emit(Response.Success(updatedSelectedTransitAgency))
                } else {
                    emit(Response.Success(selectedTransitAgencyResponseApi.successData))
                }
            }
        }
    }
        .flowOn(ioDispatcher)
}