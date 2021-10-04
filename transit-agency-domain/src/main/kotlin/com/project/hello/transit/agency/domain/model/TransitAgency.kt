package com.project.hello.transit.agency.domain.model

data class TransitAgency(
    val transitAgency: String,
    val lastUpdateFormatted: String,
    val trams: List<Line>,
    val buses: List<Line>
)