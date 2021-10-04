package com.project.hello.city.plan.domain.datasource

import com.project.hello.city.plan.domain.model.TransitAgency
import com.project.hello.commons.domain.data.ResponseApi

interface SelectedTransitAgencyDataSource {
    fun saveTransitAgency(city: TransitAgency)
    fun loadTransitAgency(): ResponseApi<TransitAgency>
}