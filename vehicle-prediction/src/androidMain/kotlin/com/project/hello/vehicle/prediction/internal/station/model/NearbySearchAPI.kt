package com.project.hello.vehicle.prediction.internal.station.model

import androidx.annotation.Keep

@Keep
internal data class NearbySearchAPI(
    val results: List<ResultAPI>,
    val status: String
)