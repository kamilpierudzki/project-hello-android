package com.project.hello.city.plan.domain.model.api

import androidx.annotation.Keep
import com.project.hello.city.plan.domain.model.CityPlan

@Keep
data class CityPlanAPI(
    val city: String,
    val lastUpdateTimestampInMillis: Long,
    val humanReadableLastUpdateTimestamp: String,
    val appVersion: Int,
    val trams: List<LineAPI>,
    val buses: List<LineAPI>
)

fun CityPlanAPI.toCityPlan() = CityPlan(
    city = city,
    lastUpdateDate = humanReadableLastUpdateTimestamp,
    trams = trams.map { it.toLine() },
    buses = buses.map { it.toLine() }
)