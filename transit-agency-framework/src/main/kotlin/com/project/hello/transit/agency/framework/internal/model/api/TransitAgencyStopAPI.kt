package com.project.hello.transit.agency.framework.internal.model.api

import androidx.annotation.Keep
import com.project.hello.transit.agency.domain.model.TransitAgencyStop

@Keep
data class TransitAgencyStopAPI(
    val transitAgency: String,
    val lastUpdateTimestampInMillis: Long,
    val lastUpdateFormatted: String,
    val dataVersion: Int,
    val tramStops: List<StopAPI>,
    val busStops: List<StopAPI>
)

fun TransitAgencyStopAPI.toTransitAgencyStop() = TransitAgencyStop(
    transitAgency = transitAgency,
    lastUpdateFormatted = lastUpdateFormatted,
    tramStops = tramStops.map { it.toStop() },
    busStops = busStops.map { it.toStop() }
)