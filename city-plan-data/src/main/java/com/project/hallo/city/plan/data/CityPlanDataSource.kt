package com.project.hallo.city.plan.data

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType

interface CityPlanDataSource {
    fun fetchPlanFor(vehicleType: VehicleType): List<Line>
}