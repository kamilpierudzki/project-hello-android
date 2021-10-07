package com.project.hello.transit.agency.framework.internal.repository

import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyStopAPI

interface TransitAgencyDataResource {
    fun saveCurrentlySelectedTransitAgency(item: TransitAgency)
    fun loadTransitAgency(fileRes: Int): ResponseApi<TransitAgencyAPI>
    fun loadTransitAgencyStop(fileRes: Int): ResponseApi<TransitAgencyStopAPI>
    fun getCurrentlySelectedTransitAgency(): ResponseApi<TransitAgency>
}