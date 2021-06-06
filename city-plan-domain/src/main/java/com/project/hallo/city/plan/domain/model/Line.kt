package com.project.hallo.city.plan.domain.model

data class Line(override val number: String, override val destination: String) : ILine {
    override fun toString(): String = "$number, $destination"
}

interface ILine {
    val number: String
    val destination: String
}