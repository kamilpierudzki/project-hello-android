package com.project.hello.transit.agency.framework.internal.repository.datasource

import com.project.hello.transit.agency.domain.repository.datasource.SelectedTransitAgencyDataSource
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.domain.model.ErrorCode.SelectedCity
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.transit.agency.framework.internal.repository.db.TransitAgencyEntity
import com.project.hello.transit.agency.framework.internal.repository.db.TransitAgencyDatabase
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