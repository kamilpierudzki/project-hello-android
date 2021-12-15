package com.project.hello.transit.agency.internal.usecase

import com.project.hello.commons.data.Response
import com.project.hello.transit.agency.model.TransitAgency
import kotlinx.coroutines.flow.Flow

interface TransitAgencySelectionUseCase {
    fun execute(transitAgency: TransitAgency): Flow<Response<TransitAgency>>
}