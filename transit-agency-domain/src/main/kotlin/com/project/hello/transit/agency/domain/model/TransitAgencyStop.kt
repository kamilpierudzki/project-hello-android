package com.project.hello.transit.agency.domain.model

data class TransitAgencyStop(
    val transitAgency: String,
    val lastUpdateFormatted: String,
    val tramStops: List<Stop>,
    val busStops: List<Stop>
)
