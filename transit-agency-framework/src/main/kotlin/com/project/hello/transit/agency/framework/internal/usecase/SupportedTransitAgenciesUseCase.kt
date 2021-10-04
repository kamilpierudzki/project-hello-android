package com.project.hello.transit.agency.framework.internal.usecase

import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData
import com.project.hello.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface SupportedTransitAgenciesUseCase {
    fun execute(): Flow<Response<SupportedTransitAgenciesData>>
}