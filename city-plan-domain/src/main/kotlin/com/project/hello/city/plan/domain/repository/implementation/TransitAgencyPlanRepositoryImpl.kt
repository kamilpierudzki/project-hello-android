package com.project.hello.city.plan.domain.repository.implementation

import com.project.hello.city.plan.domain.datasource.TransitAgencyDataSource
import com.project.hello.city.plan.domain.datasource.SelectedTransitAgencyDataSource
import com.project.hello.city.plan.domain.model.TransitAgency
import com.project.hello.city.plan.domain.model.api.City
import com.project.hello.city.plan.domain.model.api.TransitAgencyAPI
import com.project.hello.city.plan.domain.repository.TransitAgencyPlanRepository
import com.project.hello.city.plan.domain.repository.resource.TransitAgencyDataResource
import com.project.hello.commons.domain.data.ResponseApi

class TransitAgencyPlanRepositoryImpl(
    private val resourcesSource: TransitAgencyDataSource,
    private val selectedTransitAgencyDataSource: SelectedTransitAgencyDataSource
) : TransitAgencyPlanRepository {

    override fun getSupportedTransitAgenciesFileResources(): List<Int> =
        City.values().sortedBy { it.name }.map { it.file }

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