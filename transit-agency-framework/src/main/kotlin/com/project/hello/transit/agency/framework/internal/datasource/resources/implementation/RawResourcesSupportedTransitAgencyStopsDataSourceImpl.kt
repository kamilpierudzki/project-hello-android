package com.project.hello.transit.agency.framework.internal.datasource.resources.implementation

import com.project.hello.transit.agency.domain.datasource.SupportedTransitAgencyStopsDataSource
import com.project.hello.transit.agency.framework.internal.model.api.SupportedTransitStation
import javax.inject.Inject

internal class RawResourcesSupportedTransitAgencyStopsDataSourceImpl @Inject constructor():
    SupportedTransitAgencyStopsDataSource {

    override fun loadSupportedTransitAgencyStopsFileResources(): List<Int> {
        return SupportedTransitStation.values().map { it.file }
    }
}