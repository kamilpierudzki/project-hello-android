package com.project.hallo.city.plan.domain

data class CityPlanAPI(
    val city: String? = null,
    val lastUpdateTimestampInMillis: String? = null,
    val appVersion: String? = null,
    val trams: List<LineAPI>? = null
)