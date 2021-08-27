package com.project.hello.city.plan.domain.repository

import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.api.CityPlanAPI
import com.project.hello.city.plan.domain.repository.resource.CityDataResource

interface CityPlanRepository {
    fun getSupportedCityFileResources(): List<Int>
    fun getCityDataResource(): CityDataResource<CityPlan, CityPlanAPI>
}