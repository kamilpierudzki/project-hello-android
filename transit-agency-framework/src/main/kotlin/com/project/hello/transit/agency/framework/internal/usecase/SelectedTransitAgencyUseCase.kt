package com.project.hello.transit.agency.framework.internal.usecase

import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface SelectedTransitAgencyUseCase {
    fun execute(): Flow<Response<TransitAgency>>
}