package com.project.hello.city.plan.framework.internal.datasource.db

import com.project.hallo.city.plan.domain.datasource.SelectedCityDataSource
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.domain.model.ErrorCode.SelectedCity
import com.project.hallo.commons.domain.repository.Response
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

    override fun loadCity(): Response<CityPlan> {
        val dao = database.cityDao()
        val cityPlanEntity = dao.getCities().firstOrNull()
            ?: return Response.Error(SelectedCity.SELECTED_CITY_ERROR)
        val cityPlan = CityPlanEntity.toCityPlan(cityPlanEntity)
        return Response.Success(cityPlan)
    }
}