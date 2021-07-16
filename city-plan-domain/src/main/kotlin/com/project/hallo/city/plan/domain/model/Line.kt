package com.project.hallo.city.plan.domain.model

data class Line(val number: String, val destination: String) {

    override fun toString(): String = "$number, $destination"
}