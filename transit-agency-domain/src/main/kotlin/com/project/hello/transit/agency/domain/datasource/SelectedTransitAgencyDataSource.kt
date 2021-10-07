package com.project.hello.transit.agency.domain.datasource

import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.commons.domain.data.ResponseApi

interface SelectedTransitAgencyDataSource {
    fun saveTransitAgency(transitAgency: TransitAgency)
    fun loadTransitAgency(): ResponseApi<TransitAgency>
}