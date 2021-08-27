package com.project.hello.city.plan.domain.repository.implementation

import com.project.hello.city.plan.domain.datasource.CityDataSource
import com.project.hello.city.plan.domain.datasource.SelectedCityDataSource
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.api.City
import com.project.hello.city.plan.domain.model.api.CityPlanAPI
import com.project.hello.city.plan.domain.repository.CityPlanRepository
import com.project.hello.city.plan.domain.repository.resource.CityDataResource
import com.project.hello.commons.domain.data.ResponseApi

class CityPlanRepositoryImpl(
    private val resourcesSource: CityDataSource,
    private val selectedCityDataSource: SelectedCityDataSource
) : CityPlanRepository {

    override fun getSupportedCityFileResources(): List<Int> = City.values().map { it.file }

    override fun getCityDataResource() = object : CityDataResource<CityPlan, CityPlanAPI> {
        override fun load(fileRes: Int): ResponseApi<CityPlanAPI> {
            return resourcesSource.fetchCityData(fileRes)
        }

        override fun getCurrentlySelectedCity(): ResponseApi<CityPlan> {
            return selectedCityDataSource.loadCity()
        }

        override fun saveCurrentlySelectedCity(item: CityPlan) {
            selectedCityDataSource.saveCity(item)
        }
    }
}