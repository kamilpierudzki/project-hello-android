package com.project.hello.transit.agency.internal.usecase.implementation

import com.project.hello.commons.data.Response
import com.project.hello.commons.data.ResponseApi
import com.project.hello.commons.hilt.IoDispatcher
import com.project.hello.transit.agency.internal.usecase.SelectedTransitAgencyUseCase
import com.project.hello.transit.agency.model.TransitAgency
import com.project.hello.transit.agency.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.usecase.SelectedTransitAgencyUseCaseErrorMapper
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
        when (val selectedTransitAgencyResponseApi =
            transitAgencyPlanRepository.getCurrentlySelectedTransitAgency()) {
            is ResponseApi.Error -> {
                val selectedCityResponse =
                    Response.Error<TransitAgency>(selectedTransitAgencyResponseApi.rawErrorMessage)
                        .also {
                            selectedTransitAgencyUseCaseErrorMapper.mapError(it)
                        }
                emit(selectedCityResponse)
            }
            is ResponseApi.Success -> {
                val transitAgencies = transitAgencyPlanRepository.loadTransitAgencies()
                val updatedSelectedTransitAgency = transitAgencies
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