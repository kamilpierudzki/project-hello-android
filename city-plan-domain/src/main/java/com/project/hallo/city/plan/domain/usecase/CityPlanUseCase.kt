package com.project.hallo.city.plan.domain.usecase

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType
import kotlinx.coroutines.flow.Flow

interface CityPlanUseCase {
    fun getCityPlan(targetTypes: List<VehicleType>): Flow<List<Line>>
}