package com.project.hello.vehicle.prediction.framework.internal.station


import androidx.annotation.Keep

@Keep
internal data class NearbySearchAPI(
    val results: List<Result>,
    val status: String
)

@Keep
internal data class Result(
    val name: String,
    val place_id: String,
    val types: List<String>,
    val geometry: Geometry,
)

@Keep
internal data class Geometry(
    val location: Location,
)