package com.project.hello.city.plan.framework.internal.repository

interface TransitAgencyPlanRepository {
    fun getSupportedTransitAgenciesFileResources(): List<Int>
    fun getTransitAgencyDataResource(): TransitAgencyDataResource
}