package com.project.hallo.city.plan.data

import com.project.hallo.city.plan.domain.Line

interface CityPlanUseCase {
    fun getCityPlan(): List<Line>
}