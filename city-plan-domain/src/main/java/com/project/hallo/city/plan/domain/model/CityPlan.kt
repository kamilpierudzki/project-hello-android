package com.project.hallo.city.plan.domain.model

data class CityPlan(
    override val city: String,
    override val trams: List<Line>,
    override val buses: List<Line>
) : ICityPlan

interface ICityPlan {
    val city: String
    val trams: List<Line>
    val buses: List<Line>
}