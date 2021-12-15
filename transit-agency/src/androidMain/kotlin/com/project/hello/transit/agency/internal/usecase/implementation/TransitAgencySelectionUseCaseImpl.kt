package com.project.hello.transit.agency.internal.usecase.implementation

import com.project.hello.commons.data.Response
import com.project.hello.commons.hilt.IoDispatcher
import com.project.hello.transit.agency.internal.usecase.TransitAgencySelectionUseCase
import com.project.hello.transit.agency.model.TransitAgency
import com.project.hello.transit.agency.repository.TransitAgencyPlanRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class TransitAgencySelectionUseCaseImpl @Inject constructor(
    private val transitAgencyPlanRepository: TransitAgencyPlanRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TransitAgencySelectionUseCase {

    override fun execute(transitAgency: TransitAgency): Flow<Response<TransitAgency>> = flow {
        emit(Response.Loading())
        transitAgencyPlanRepository.saveCurrentlySelectedTransitAgency(transitAgency)
        emit(Response.Success(transitAgency))
    }
        .flowOn(ioDispatcher)
}