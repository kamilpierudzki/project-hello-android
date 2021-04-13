package com.project.hallo.city.plan.domain.usecase

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType

interface CityPlanUseCase {
    fun getCityPlan(targetTypes: List<VehicleType>): List<Line>
}