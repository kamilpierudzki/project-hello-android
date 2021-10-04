package com.project.hello.city.plan.domain.model

data class TransitAgencyStop(
    val transitAgency: String,
    val lastUpdateFormatted: String,
    val tramStops: List<Stop>,
    val busStops: List<Stop>
)
