package com.project.hallo.city.plan.domain.repository

import com.project.hallo.city.plan.domain.datasource.CityDataSource
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.api.CityPlanAPI
import com.project.hallo.city.plan.domain.repository.resource.CityDataResource
import com.project.hallo.commons.domain.repository.Response

class CityPlanRepository(
    private val resourcesSource: CityDataSource
) {

    fun getCityDataResource(city: String) = object : CityDataResource<CityPlan, CityPlanAPI> {
        override fun saveFetchResult(item: CityPlanAPI) {
            // todo implement
        }

        override fun fetch(): Response<CityPlanAPI> {
            return resourcesSource.fetchCityData(city)
        }

        override fun loadFromDb(): Response<CityPlan> {
            return Response.Error("no data in DB")
        }
    }
}