package com.project.hello.city.plan.domain.usecase

import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface CitySelectionUseCase {
    fun execute(city: CityPlan): Flow<Response<CityPlan>>
}
