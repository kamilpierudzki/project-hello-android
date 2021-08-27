package com.project.hello.city.plan.domain.model.api

import com.project.hello.city.plan.domain.model.CityPlan

data class CityPlanAPI(
    val city: String? = null,
    val lastUpdateTimestampInMillis: String? = null,
    val appVersion: String? = null,
    val trams: List<LineAPI> = emptyList(),
    val buses: List<LineAPI> = emptyList()
)

fun CityPlanAPI.toCityPlan() = CityPlan(
    city = city ?: "",
    trams = trams.map { it.toLine() },
    buses = buses.map { it.toLine() }
)