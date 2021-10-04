package com.project.hello.city.plan.domain.datasource

import com.project.hello.city.plan.domain.model.api.TransitAgencyAPI
import com.project.hello.commons.domain.data.ResponseApi

interface TransitAgencyDataSource {
    fun fetchTransitAgencyData(resFile: Int): ResponseApi<TransitAgencyAPI>
}