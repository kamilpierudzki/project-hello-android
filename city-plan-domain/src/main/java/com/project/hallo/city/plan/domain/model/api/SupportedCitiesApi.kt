package com.project.hallo.city.plan.domain.model.api

import com.project.hallo.city.plan.domain.model.SupportedCitiesData

data class SupportedCitiesApi(
    val lastUpdateTimestampInMillis: String? = null,
    val appVersion: String? = null,
    val supportedCities: List<String>? = null
)

fun SupportedCitiesApi.toSupportedCitiesData(): SupportedCitiesData {
    return SupportedCitiesData(supportedCities ?: emptyList())
}