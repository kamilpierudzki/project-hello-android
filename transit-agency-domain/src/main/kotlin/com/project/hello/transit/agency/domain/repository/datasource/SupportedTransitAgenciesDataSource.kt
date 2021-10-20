package com.project.hello.transit.agency.domain.repository.datasource

interface SupportedTransitAgenciesDataSource {
    fun loadSupportedTransitAgenciesFileResources(): List<Int>
}