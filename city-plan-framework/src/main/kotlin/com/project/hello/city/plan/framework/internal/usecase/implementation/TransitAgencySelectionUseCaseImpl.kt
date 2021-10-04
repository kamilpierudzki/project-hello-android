package com.project.hello.city.plan.framework.internal.usecase.implementation

import com.project.hello.city.plan.domain.model.TransitAgency
import com.project.hello.city.plan.domain.repository.TransitAgencyPlanRepository
import com.project.hello.city.plan.framework.internal.usecase.TransitAgencySelectionUseCase
import com.project.hello.commons.domain.data.Response
import com.project.hello.commons.framework.hilt.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class TransitAgencySelectionUseCaseImpl @Inject constructor(
    private val transitAgencyPlanRepository: TransitAgencyPlanRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TransitAgencySelectionUseCase {

    override fun execute(transitAgency: TransitAgency): Flow<Response<TransitAgency>> = flow {
        emit(Response.Loading())
        val cityDataResource = transitAgencyPlanRepository.getTransitAgencyDataResource()
        cityDataResource.saveCurrentlySelectedTransitAgency(transitAgency)
        emit(Response.Success(transitAgency))
    }
        .flowOn(ioDispatcher)
}