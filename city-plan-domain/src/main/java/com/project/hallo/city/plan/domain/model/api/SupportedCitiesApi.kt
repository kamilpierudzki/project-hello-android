package com.project.hallo.city.plan.domain.model.api

data class SupportedCitiesApi(
    val lastUpdateTimestampInMillis: String? = null,
    val appVersion: String? = null,
    val supportedCities: List<String>? = null
)