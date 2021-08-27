package com.project.hello.city.plan.domain.datasource

import com.project.hello.city.plan.domain.model.api.CityPlanAPI
import com.project.hello.commons.domain.data.ResponseApi

interface CityDataSource {
    fun fetchCityData(resFile: Int): ResponseApi<CityPlanAPI>
}