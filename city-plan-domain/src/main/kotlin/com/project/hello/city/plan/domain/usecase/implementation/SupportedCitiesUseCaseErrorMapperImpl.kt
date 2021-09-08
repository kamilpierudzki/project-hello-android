package com.project.hello.city.plan.domain.usecase.implementation

import android.content.res.Resources
import com.project.hello.city.plan.domain.R
import com.project.hello.city.plan.domain.model.ErrorCode
import com.project.hello.city.plan.domain.model.SupportedCitiesData
import com.project.hello.city.plan.domain.usecase.SupportedCitiesUseCaseErrorMapper
import com.project.hello.commons.domain.data.Response

class SupportedCitiesUseCaseErrorMapperImpl(private val resources: Resources) :
    SupportedCitiesUseCaseErrorMapper { // todo move into a framework module

    override fun mapError(error: Response.Error<SupportedCitiesData>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            ErrorCode.SupportedCities.SUPPORTED_CITIES_ERROR ->
                resources.getString(R.string.error_load_supported_cities)
            else -> error.rawErrorMessage
        }
    }
}