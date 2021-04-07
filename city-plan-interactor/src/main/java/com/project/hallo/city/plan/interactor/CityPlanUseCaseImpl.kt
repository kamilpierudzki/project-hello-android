package com.project.hallo.city.plan.interactor

import com.project.hallo.city.plan.data.CityPlanRepository
import com.project.hallo.city.plan.data.CityPlanUseCase
import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType

class CityPlanUseCaseImpl(
    private val cityPlanRepository: CityPlanRepository
) : CityPlanUseCase {

    override fun getCityPlan(): List<Line> {
        return cityPlanRepository.getCityPlan(VehicleType.TRAM)
    }
}