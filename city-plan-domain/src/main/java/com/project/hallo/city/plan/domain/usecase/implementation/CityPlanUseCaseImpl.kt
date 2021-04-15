package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.CityPlanRepository
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase
import kotlinx.coroutines.flow.flow

class CityPlanUseCaseImpl(private val cityPlanRepository: CityPlanRepository) : CityPlanUseCase {

    override fun getCityPlan(targetTypes: List<VehicleType>) = flow {
        val lines = cityPlanRepository.getCityPlan(targetTypes)
        emit(lines)
    }
}