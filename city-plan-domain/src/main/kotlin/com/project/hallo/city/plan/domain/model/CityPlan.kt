package com.project.hallo.city.plan.domain.model

data class CityPlan(
    val city: String,
    val trams: List<Line>,
    val buses: List<Line>
)