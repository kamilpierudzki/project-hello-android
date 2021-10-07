package com.project.hello.transit.agency.domain.datasource

interface SupportedTransitAgencyStopsDataSource {
    fun loadSupportedTransitAgencyStopsFileResources(): List<Int>
}