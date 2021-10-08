package com.project.hello.transit.agency.framework.internal.datasource.resources

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI

interface TransitAgencyDataSource {
    fun fetchTransitAgencyData(resFile: Int): ResponseApi<TransitAgencyAPI>
}