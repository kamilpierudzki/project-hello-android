package com.project.hallo.city.plan.domain.usecase

import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface SelectedCityUseCase {
    fun execute(): Flow<Response<CityPlan>>
}

interface SelectedCityUseCaseErrorMapper {
    fun mapError(error: Response.Error<CityPlan>)
}