package com.project.hello.city.plan.domain.usecase

import com.project.hello.city.plan.domain.model.SupportedCitiesData
import com.project.hello.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface SupportedCitiesUseCase {
    fun execute(): Flow<Response<SupportedCitiesData>>
}

interface SupportedCitiesUseCaseErrorMapper {
    fun mapError(error: Response.Error<SupportedCitiesData>)
}