package com.project.hello.transit.agency.repository.datasource

interface SupportedTransitAgenciesDataSource {
    fun loadSupportedTransitAgenciesFileResources(): List<Int>
}