package com.project.hello.transit.agency.domain.model

object ErrorCode {
    object SelectedCity {
        val SELECTED_CITY_ERROR = "Couldn't load CityPlanEntity"
    }

    object SupportedCities {
        val SUPPORTED_CITIES_ERROR = "Couldn't get supported cities"
    }
}