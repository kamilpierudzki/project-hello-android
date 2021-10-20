package com.project.hello.transit.agency.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.framework.hilt.IoDispatcher
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.domain.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.framework.internal.usecase.TransitAgencySelectionUseCase
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