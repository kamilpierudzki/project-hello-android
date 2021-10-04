package com.project.hello.transit.agency.framework.internal.repository

import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.commons.domain.data.ResponseApi

interface TransitAgencyDataResource {
    fun saveCurrentlySelectedTransitAgency(item: TransitAgency)
    fun load(fileRes: Int): ResponseApi<TransitAgencyAPI>
    fun getCurrentlySelectedTransitAgency(): ResponseApi<TransitAgency>
}