package com.project.hello.transit.agency.domain.datasource

interface SupportedTransitAgenciesDataSource {
    fun loadSupportedTransitAgenciesFileResources(): List<Int>
}