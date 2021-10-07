package com.project.hello.transit.agency.domain.model

data class TransitAgency(
    val transitAgency: String,
    val lastUpdateFormatted: String,
    val tramLines: List<Line>,
    val busLines: List<Line>,
    val tramStops: List<Stop>,
    val busStops: List<Stop>,
)