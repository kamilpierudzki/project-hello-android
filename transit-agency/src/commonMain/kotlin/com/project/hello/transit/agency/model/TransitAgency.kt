package com.project.hello.transit.agency.model

import com.project.hello.transit.agency.model.Line
import com.project.hello.transit.agency.model.Stop

data class TransitAgency(
    val transitAgency: String,
    val lastUpdateFormatted: String,
    val tramLines: List<Line>,
    val busLines: List<Line>,
    val tramStops: List<Stop>,
    val busStops: List<Stop>,
)