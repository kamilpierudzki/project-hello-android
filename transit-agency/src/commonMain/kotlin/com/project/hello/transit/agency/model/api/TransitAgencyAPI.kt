package com.project.hello.transit.agency.model.api

import com.project.hello.transit.agency.model.TransitAgency

data class TransitAgencyAPI(
    val transitAgency: String,
    val lastUpdateTimestampInMillis: Long,
    val lastUpdateFormatted: String,
    val dataVersion: Int,
    val tramLines: List<LineAPI>,
    val busLines: List<LineAPI>,
    val tramStops: List<StopAPI>,
    val busStops: List<StopAPI>,
)

fun TransitAgencyAPI.toTransitAgency() = TransitAgency(
    transitAgency = transitAgency,
    lastUpdateFormatted = lastUpdateFormatted,
    tramLines = tramLines.map { it.toLine() },
    busLines = busLines.map { it.toLine() },
    tramStops = tramStops.map { it.toStop() },
    busStops = busStops.map { it.toStop() },
)