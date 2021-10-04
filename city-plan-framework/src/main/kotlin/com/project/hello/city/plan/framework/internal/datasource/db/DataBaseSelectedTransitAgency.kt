package com.project.hello.city.plan.framework.internal.datasource.db

import com.project.hello.transit.agency.domain.datasource.SelectedTransitAgencyDataSource
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.domain.model.ErrorCode.SelectedCity
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.city.plan.framework.internal.db.TransitAgencyDatabase
import javax.inject.Inject

internal class DataBaseSelectedTransitAgency @Inject constructor(
    private val database: TransitAgencyDatabase
) : SelectedTransitAgencyDataSource {

    override fun saveTransitAgency(city: TransitAgency) {
        val dao = database.transitAgencyDao()
        dao.deleteAll()
        val entity = TransitAgencyEntity.fromCityPlan(city)
        dao.insertTransitAgency(entity)
    }

    override fun loadTransitAgency(): ResponseApi<TransitAgency> {
        val dao = database.transitAgencyDao()
        val cityPlanEntity = dao.getTransitAgencies().firstOrNull()
            ?: return ResponseApi.Error(SelectedCity.SELECTED_CITY_ERROR)
        val cityPlan = TransitAgencyEntity.toCityPlan(cityPlanEntity)
        return ResponseApi.Success(cityPlan)
    }
}