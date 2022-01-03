package com.project.hello.transit.agency.internal.repository.datasource

import com.project.hello.transit.agency.internal.model.SupportedTransitAgency
import com.project.hello.transit.agency.repository.datasource.SupportedTransitAgenciesDataSource
import javax.inject.Inject

internal class LocalSupportedTransitAgenciesDataSource @Inject constructor() :
    SupportedTransitAgenciesDataSource {

    override fun loadSupportedTransitAgenciesFileResources(): List<Int> {
        return SupportedTransitAgency.values().map { it.file }
    }
}