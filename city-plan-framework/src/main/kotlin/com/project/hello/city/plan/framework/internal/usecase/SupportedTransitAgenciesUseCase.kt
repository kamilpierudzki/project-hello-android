package com.project.hello.city.plan.framework.internal.usecase

import com.project.hello.city.plan.domain.model.SupportedTransitAgenciesData
import com.project.hello.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface SupportedTransitAgenciesUseCase {
    fun execute(): Flow<Response<SupportedTransitAgenciesData>>
}