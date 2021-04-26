package com.project.hallo.city.plan.domain.usecase

import com.project.hallo.city.plan.domain.model.SupportedCitiesData
import com.project.hallo.commons.domain.repository.Response
import kotlinx.coroutines.flow.Flow

interface SupportedCitiesUseCase {
    fun execute(): Flow<Response<SupportedCitiesData>>
}