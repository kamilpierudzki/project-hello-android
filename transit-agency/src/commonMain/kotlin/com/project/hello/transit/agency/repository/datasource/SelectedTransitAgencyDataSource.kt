package com.project.hello.transit.agency.repository.datasource

import com.project.hello.commons.data.ResponseApi
import com.project.hello.transit.agency.model.TransitAgency

interface SelectedTransitAgencyDataSource {
    fun saveTransitAgency(transitAgency: TransitAgency)
    fun loadTransitAgency(): ResponseApi<TransitAgency>
}