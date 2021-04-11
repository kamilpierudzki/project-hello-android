package com.project.hallo.city.plan.domain

data class Line(
    val number: String,
    val destinationVariants: List<String>
) {

    val destination: String = destinationVariants.firstOrNull() ?: ""

    override fun toString(): String =
        "$number, $destination"

    companion object {
        fun fromLineAPI(api: LineAPI): Line {
            return Line(
                number = api.number ?: "",
                destinationVariants = api.destinationVariants ?: emptyList()
            )
        }
    }
}