package com.project.hallo.city.plan.domain

data class Line(
    val number: String,
    val destinationVariants: List<String>,
    val vehicleType: VehicleType
) {

    val destination: String = destinationVariants.firstOrNull() ?: ""

    override fun toString(): String =
        "$vehicleType, $number, $destination"
}