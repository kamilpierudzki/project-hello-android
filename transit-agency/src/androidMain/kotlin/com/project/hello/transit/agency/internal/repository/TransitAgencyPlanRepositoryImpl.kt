package com.project.hello.transit.agency.internal.repository

import com.project.hello.commons.data.ResponseApi
import com.project.hello.transit.agency.internal.model.api.toTransitAgency
import com.project.hello.transit.agency.internal.repository.datasource.LocalTransitAgencyDataSource
import com.project.hello.transit.agency.model.TransitAgency
import com.project.hello.transit.agency.repository.TransitAgencyPlanRepository
import com.project.hello.transit.agency.repository.datasource.SelectedTransitAgencyDataSource
import com.project.hello.transit.agency.repository.datasource.SupportedTransitAgenciesDataSource
import javax.inject.Inject

internal class TransitAgencyPlanRepositoryImpl @Inject constructor(
    private val localTransitAgencyDataSource: LocalTransitAgencyDataSource,
    private val selectedTransitAgencyDataSource: SelectedTransitAgencyDataSource,
    private val localSupportedTransitAgenciesDataSource: SupportedTransitAgenciesDataSource,
) : TransitAgencyPlanRepository {

    override fun loadTransitAgencies(): List<TransitAgency> {
        return localSupportedTransitAgenciesDataSource.loadSupportedTransitAgenciesFileResources()
            .map { localTransitAgencyDataSource.loadTransitAgency(it) }
            .asSequence()
            .filter { it is ResponseApi.Success }
            .map { (it as ResponseApi.Success).successData }
            .map { it.toTransitAgency() }
            .toList()
    }

    override fun getCurrentlySelectedTransitAgency(): ResponseApi<TransitAgency> {
        return selectedTransitAgencyDataSource.loadTransitAgency()
    }

    override fun saveCurrentlySelectedTransitAgency(item: TransitAgency) {
        selectedTransitAgencyDataSource.saveTransitAgency(item)
    }
}