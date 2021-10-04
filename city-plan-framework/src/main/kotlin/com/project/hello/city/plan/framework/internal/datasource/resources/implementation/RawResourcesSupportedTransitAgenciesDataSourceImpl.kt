package com.project.hello.city.plan.framework.internal.datasource.resources.implementation

import com.project.hello.city.plan.domain.datasource.SupportedTransitAgenciesDataSource
import com.project.hello.city.plan.framework.internal.model.api.City
import javax.inject.Inject

internal class RawResourcesSupportedTransitAgenciesDataSourceImpl @Inject constructor():
    SupportedTransitAgenciesDataSource {

    override fun loadSupportedTransitAgenciesFileResources(): List<Int> {
        return City.values().sortedBy { it.name }.map { it.file }
    }
}