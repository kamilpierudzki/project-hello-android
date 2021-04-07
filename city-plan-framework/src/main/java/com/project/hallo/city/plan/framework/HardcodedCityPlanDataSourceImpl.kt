package com.project.hallo.city.plan.framework

import com.project.hallo.city.plan.data.CityPlanDataSource
import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.city.Poznan

class HardcodedCityPlanDataSourceImpl : CityPlanDataSource {

    override fun fetchPlanFor(vehicleType: VehicleType): List<Line> {
        return when (vehicleType) {
            VehicleType.TRAM -> Poznan().getPlanForTram()
            else -> emptyList()
        }
    }
}