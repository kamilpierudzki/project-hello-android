package com.project.hallo.city.plan.framework.internal.datasource

import com.project.hallo.city.plan.domain.CityPlanAPI
import com.project.hallo.city.plan.domain.CityPlanDataSource
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.domain.city.Poznan

class HardcodedCityPlanDataSourceImpl : CityPlanDataSource {

    override fun fetchPlanFor(vehicleType: VehicleType): CityPlanAPI {
        val city = Poznan()
        val currentTimestampInMillis: Long = System.currentTimeMillis()
        val cityPlanApi = CityPlanAPI(
            "poznań",
            "$currentTimestampInMillis",
            "1",
            city.getPlanForTram()
        )
        return cityPlanApi
    }
}