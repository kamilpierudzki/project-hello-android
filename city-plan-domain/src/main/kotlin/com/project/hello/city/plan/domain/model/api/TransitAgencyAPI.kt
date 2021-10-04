package com.project.hello.city.plan.domain.model.api

import androidx.annotation.Keep
import com.project.hello.city.plan.domain.model.TransitAgency

@Keep
data class TransitAgencyAPI(
    val transitAgency: String,
    val lastUpdateTimestampInMillis: Long,
    val lastUpdateFormatted: String,
    val dataVersion: Int,
    val trams: List<LineAPI>,
    val buses: List<LineAPI>
)

fun TransitAgencyAPI.toCityPlan() = TransitAgency(
    transitAgency = transitAgency,
    lastUpdateFormatted = lastUpdateFormatted,
    trams = trams.map { it.toLine() },
    buses = buses.map { it.toLine() }
)