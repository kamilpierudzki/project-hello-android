package com.project.hello.city.plan.domain.datasource

interface SupportedTransitAgenciesDataSource {
    fun loadSupportedTransitAgenciesFileResources(): List<Int>
}