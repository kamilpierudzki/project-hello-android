package com.project.hello.transit.agency.internal.usecase

import com.project.hello.commons.data.Response
import com.project.hello.transit.agency.model.SupportedTransitAgenciesData
import kotlinx.coroutines.flow.Flow

interface SupportedTransitAgenciesUseCase {
    fun execute(): Flow<Response<SupportedTransitAgenciesData>>
}