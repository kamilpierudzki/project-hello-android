package com.project.hello.transit.agency.repository

import com.project.hello.commons.data.ResponseApi
import com.project.hello.transit.agency.model.TransitAgency

interface TransitAgencyPlanRepository {
    fun saveCurrentlySelectedTransitAgency(item: TransitAgency)
    fun loadTransitAgencies(): List<TransitAgency>
    fun getCurrentlySelectedTransitAgency(): ResponseApi<TransitAgency>
}