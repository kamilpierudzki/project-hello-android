package com.project.hello.city.plan.framework.internal.repository

import com.project.hello.city.plan.domain.model.TransitAgency
import com.project.hello.city.plan.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.commons.domain.data.ResponseApi

interface TransitAgencyDataResource {
    fun saveCurrentlySelectedTransitAgency(item: TransitAgency)
    fun load(fileRes: Int): ResponseApi<TransitAgencyAPI>
    fun getCurrentlySelectedTransitAgency(): ResponseApi<TransitAgency>
}