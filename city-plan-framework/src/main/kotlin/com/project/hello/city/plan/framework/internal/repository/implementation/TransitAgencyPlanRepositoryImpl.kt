package com.project.hello.city.plan.framework.internal.repository.implementation

import com.project.hello.transit.agency.domain.datasource.SelectedTransitAgencyDataSource
import com.project.hello.transit.agency.domain.datasource.SupportedTransitAgenciesDataSource
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.city.plan.framework.internal.datasource.resources.TransitAgencyDataSource
import com.project.hello.city.plan.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.city.plan.framework.internal.repository.TransitAgencyDataResource
import com.project.hello.city.plan.framework.internal.repository.TransitAgencyPlanRepository
import com.project.hello.commons.domain.data.ResponseApi
import javax.inject.Inject

internal class TransitAgencyPlanRepositoryImpl @Inject constructor(
    private val resourcesSource: TransitAgencyDataSource,
    private val selectedTransitAgencyDataSource: SelectedTransitAgencyDataSource,
    private val supportedTransitAgenciesDataSource: SupportedTransitAgenciesDataSource,
) : TransitAgencyPlanRepository {

    override fun getSupportedTransitAgenciesFileResources(): List<Int> {
        return supportedTransitAgenciesDataSource.loadSupportedTransitAgenciesFileResources()
    }

    override fun getTransitAgencyDataResource() = object : TransitAgencyDataResource {
        override fun load(fileRes: Int): ResponseApi<TransitAgencyAPI> {
            return resourcesSource.fetchTransitAgencyData(fileRes)
        }

        override fun getCurrentlySelectedTransitAgency(): ResponseApi<TransitAgency> {
            return selectedTransitAgencyDataSource.loadTransitAgency()
        }

        override fun saveCurrentlySelectedTransitAgency(item: TransitAgency) {
            selectedTransitAgencyDataSource.saveTransitAgency(item)
        }
    }
}