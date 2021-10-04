package com.project.hello.city.plan.framework.internal.usecase

import com.project.hello.city.plan.domain.model.TransitAgency
import com.project.hello.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface SelectedTransitAgencyUseCase {
    fun execute(): Flow<Response<TransitAgency>>
}