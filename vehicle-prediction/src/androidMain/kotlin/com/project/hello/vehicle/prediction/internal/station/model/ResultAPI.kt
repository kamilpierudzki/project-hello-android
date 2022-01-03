package com.project.hello.vehicle.prediction.internal.station.model

import androidx.annotation.Keep

@Keep
internal data class ResultAPI(
    val name: String,
    val place_id: String,
    val types: List<String>,
    val geometryAPI: GeometryAPI,
)