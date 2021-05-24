package com.project.hallo.city.plan.domain.usecase.implementation

import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.model.Line
import com.project.hallo.city.plan.domain.repository.CityPlanRepository
import com.project.hallo.city.plan.domain.usecase.CityPlanUseCase
import kotlinx.coroutines.flow.flow

class CityPlanUseCaseImpl(private val cityPlanRepository: CityPlanRepository) : CityPlanUseCase {

    override fun getCityPlan(targetTypes: List<VehicleType>) = flow {
        val lines = listOf(Line("", "")) // todo implement
        emit(lines)
    }
}