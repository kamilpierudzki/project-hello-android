package com.project.hello.transit.agency.framework.internal.model.api

import androidx.annotation.Keep

@Keep
data class TransitAgencyStopAPI(
    val transitAgency: String,
    val lastUpdateTimestampInMillis: Long,
    val lastUpdateFormatted: String,
    val dataVersion: Int,
    val tramStops: List<StopAPI>,
    val busStops: List<StopAPI>
)