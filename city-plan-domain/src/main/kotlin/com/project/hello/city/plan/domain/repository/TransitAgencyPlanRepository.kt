package com.project.hello.city.plan.domain.repository

import com.project.hello.city.plan.domain.repository.resource.TransitAgencyDataResource

interface TransitAgencyPlanRepository {
    fun getSupportedTransitAgenciesFileResources(): List<Int>
    fun getTransitAgencyDataResource(): TransitAgencyDataResource
}