package com.project.hello.transit.agency.framework.internal.datasource.resources.implementation

import com.project.hello.transit.agency.domain.datasource.SupportedTransitAgenciesDataSource
import com.project.hello.transit.agency.framework.internal.model.api.SupportedTransitAgency
import javax.inject.Inject

internal class RawResourcesSupportedTransitAgenciesDataSourceImpl @Inject constructor() :
    SupportedTransitAgenciesDataSource {

    override fun loadSupportedTransitAgenciesFileResources(): List<Int> {
        return SupportedTransitAgency.values().map { it.file }
    }
}