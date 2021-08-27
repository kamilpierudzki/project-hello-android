package com.project.hello.city.plan.domain.datasource

import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.commons.domain.data.ResponseApi

interface SelectedCityDataSource {
    fun saveCity(city: CityPlan)
    fun loadCity(): ResponseApi<CityPlan>
}