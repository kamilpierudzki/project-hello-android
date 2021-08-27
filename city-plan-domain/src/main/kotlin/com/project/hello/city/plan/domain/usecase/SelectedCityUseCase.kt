package com.project.hello.city.plan.domain.usecase

import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface SelectedCityUseCase {
    fun execute(): Flow<Response<CityPlan>>
}

interface SelectedCityUseCaseErrorMapper {
    fun mapError(error: Response.Error<CityPlan>)
}