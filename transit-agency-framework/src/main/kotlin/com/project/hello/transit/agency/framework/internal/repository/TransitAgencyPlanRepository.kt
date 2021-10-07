package com.project.hello.transit.agency.framework.internal.repository

interface TransitAgencyPlanRepository {
    fun getSupportedTransitAgenciesFileResources(): List<Int>
    fun getSupportedTransitAgencyStopsFileResources(): List<Int>
    fun getTransitAgencyDataResource(): TransitAgencyDataResource
}