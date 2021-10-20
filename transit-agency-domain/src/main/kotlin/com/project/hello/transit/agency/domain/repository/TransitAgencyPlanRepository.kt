package com.project.hello.transit.agency.domain.repository

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.transit.agency.domain.model.TransitAgency

interface TransitAgencyPlanRepository {
    fun saveCurrentlySelectedTransitAgency(item: TransitAgency)
    fun loadTransitAgencies(): List<TransitAgency>
    fun getCurrentlySelectedTransitAgency(): ResponseApi<TransitAgency>
}