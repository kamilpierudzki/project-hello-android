package com.project.hello.transit.station.framework.internal


import androidx.annotation.Keep

@Keep
data class NearbySearchAPI(
    val results: List<Result>,
    val status: String
)

@Keep
data class Result(
    val name: String,
    val place_id: String,
    val types: List<String>,
    val geometry: Geometry,
)

@Keep
data class Geometry(
    val location: Location,
)