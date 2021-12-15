package com.project.hello.transit.agency.internal.usecase

import com.project.hello.commons.data.Response
import com.project.hello.transit.agency.model.TransitAgency
import kotlinx.coroutines.flow.Flow

interface SelectedTransitAgencyUseCase {
    fun execute(): Flow<Response<TransitAgency>>
}