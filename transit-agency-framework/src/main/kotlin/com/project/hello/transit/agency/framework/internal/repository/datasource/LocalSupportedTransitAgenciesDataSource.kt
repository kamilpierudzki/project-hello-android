package com.project.hello.transit.agency.framework.internal.repository.datasource

import com.project.hello.transit.agency.domain.repository.datasource.SupportedTransitAgenciesDataSource
import com.project.hello.transit.agency.framework.internal.model.api.SupportedTransitAgency
import javax.inject.Inject

internal class LocalSupportedTransitAgenciesDataSource @Inject constructor() :
    SupportedTransitAgenciesDataSource {

    override fun loadSupportedTransitAgenciesFileResources(): List<Int> {
        return SupportedTransitAgency.values().map { it.file }
    }
}