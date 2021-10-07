package com.project.hello.transit.agency.framework.internal.model.api

import androidx.annotation.Keep
import com.project.hello.transit.agency.domain.model.TransitAgency

@Keep
data class TransitAgencyAPI(
    val transitAgency: String,
    val lastUpdateTimestampInMillis: Long,
    val lastUpdateFormatted: String,
    val dataVersion: Int,
    val trams: List<LineAPI>,
    val buses: List<LineAPI>
)

fun TransitAgencyAPI.toTransitAgency(transitAgencyStopAPI: TransitAgencyStopAPI) = TransitAgency(
    transitAgency = transitAgency,
    lastUpdateFormatted = lastUpdateFormatted,
    tramLines = trams.map { it.toLine() },
    busLines = buses.map { it.toLine() },
    tramStops = transitAgencyStopAPI.tramStops.map { it.toStop() },
    busStops = transitAgencyStopAPI.busStops.map { it.toStop() },
)