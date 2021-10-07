package com.project.hello.transit.agency.framework.internal.datasource.resources

import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyStopAPI

interface TransitAgencyDataSource {
    fun fetchTransitAgencyData(resFile: Int): ResponseApi<TransitAgencyAPI>
    fun fetchTransitAgencyStopData(resFile: Int): ResponseApi<TransitAgencyStopAPI>
}