package com.project.hello.city.plan.framework.internal.datasource.db

import com.project.hello.city.plan.domain.datasource.SelectedCityDataSource
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.ErrorCode.SelectedCity
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.city.plan.framework.internal.db.CityDatabase
import javax.inject.Inject

internal class DataBaseSelectedCity @Inject constructor(
    private val database: CityDatabase
) : SelectedCityDataSource {

    override fun saveCity(city: CityPlan) {
        val dao = database.cityDao()
        dao.deleteAll()
        val entity = CityPlanEntity.fromCityPlan(city)
        dao.insertCity(entity)
    }

    override fun loadCity(): ResponseApi<CityPlan> {
        val dao = database.cityDao()
        val cityPlanEntity = dao.getCities().firstOrNull()
            ?: return ResponseApi.Error(SelectedCity.SELECTED_CITY_ERROR)
        val cityPlan = CityPlanEntity.toCityPlan(cityPlanEntity)
        return ResponseApi.Success(cityPlan)
    }
}