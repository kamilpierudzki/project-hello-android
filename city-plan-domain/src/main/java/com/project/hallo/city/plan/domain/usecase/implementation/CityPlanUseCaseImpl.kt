package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.CityPlanRepository
import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase

class CityPlanUseCaseImpl(
    private val cityPlanRepository: CityPlanRepository
) : CityPlanUseCase {

    override fun getCityPlan(): List<Line> {
        return cityPlanRepository.getCityPlan(VehicleType.TRAM)
    }
}