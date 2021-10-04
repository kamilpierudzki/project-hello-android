package com.project.hello.transit.agency.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.commons.framework.hilt.IoDispatcher
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.domain.usecase.SelectedTransitAgencyUseCaseErrorMapper
import com.project.hello.transit.agency.framework.internal.model.api.toCityPlan
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
                val updatedSelectedCity: TransitAgency? =
                    transitAgencyPlanRepository.getSupportedTransitAgenciesFileResources()
                        .map { supportedCityFile -> transitAgencyDataResource.load(supportedCityFile) }
                        .mapNotNull {
                            it.data
                        }
                        .map { it.toCityPlan() }
                        .firstOrNull {
                            it.transitAgency == selectedTransitAgencyResponseApi.successData.transitAgency
                        }
                if (updatedSelectedCity != null) {
                    emit(Response.Success(updatedSelectedCity))
                } else {
                    emit(Response.Success(selectedTransitAgencyResponseApi.successData))
                }
            }
        }
    }
        .flowOn(ioDispatcher)
}