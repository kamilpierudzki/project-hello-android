package com.project.hallo.city.plan.domain.model

import com.project.hallo.city.plan.domain.model.api.LineAPI

data class Line(
    val number: String,
    val destination: String
) {

    override fun toString(): String =
        "$number, $destination"

    companion object {
        fun fromLineAPI(api: LineAPI): Line {
            return Line(
                number = api.number ?: "",
                destination = api.destination ?: ""
            )
        }
    }
}