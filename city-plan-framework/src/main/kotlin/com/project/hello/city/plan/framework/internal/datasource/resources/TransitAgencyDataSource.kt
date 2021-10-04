package com.project.hello.city.plan.framework.internal.datasource.resources

import com.project.hello.city.plan.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.commons.domain.data.ResponseApi

interface TransitAgencyDataSource {
    fun fetchTransitAgencyData(resFile: Int): ResponseApi<TransitAgencyAPI>
}