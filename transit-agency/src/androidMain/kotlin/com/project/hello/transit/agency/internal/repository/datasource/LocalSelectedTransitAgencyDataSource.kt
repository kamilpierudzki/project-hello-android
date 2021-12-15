package com.project.hello.transit.agency.internal.repository.datasource

import com.project.hello.commons.data.ResponseApi
import com.project.hello.transit.agency.internal.repository.db.TransitAgencyDatabase
import com.project.hello.transit.agency.internal.repository.db.TransitAgencyEntity
import com.project.hello.transit.agency.model.ErrorCode.SelectedCity
import com.project.hello.transit.agency.model.TransitAgency
import com.project.hello.transit.agency.repository.datasource.SelectedTransitAgencyDataSource
import javax.inject.Inject

internal class LocalSelectedTransitAgencyDataSource @Inject constructor(
    private val database: TransitAgencyDatabase
) : SelectedTransitAgencyDataSource {

    override fun saveTransitAgency(transitAgency: TransitAgency) {
        val dao = database.transitAgencyDao()
        dao.deleteAll()
        val entity = TransitAgencyEntity.fromTransitAgency(transitAgency)
        dao.insertTransitAgency(entity)
    }

    override fun loadTransitAgency(): ResponseApi<TransitAgency> {
        val dao = database.transitAgencyDao()
        val cityPlanEntity = dao.getTransitAgencies().firstOrNull()
            ?: return ResponseApi.Error(SelectedCity.SELECTED_CITY_ERROR)
        val cityPlan = TransitAgencyEntity.toTransitAgency(cityPlanEntity)
        return ResponseApi.Success(cityPlan)
    }
}