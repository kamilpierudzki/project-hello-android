package com.project.hello.city.plan.framework.internal.usecase

import android.content.res.Resources
import com.project.hello.city.plan.framework.R
import com.project.hello.commons.domain.data.Response
import com.project.hello.transit.agency.domain.model.ErrorCode
import com.project.hello.transit.agency.domain.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.domain.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import javax.inject.Inject

internal class SupportedTransitAgenciesUseCaseErrorMapperImpl @Inject constructor(
    private val resources: Resources
) : SupportedTransitAgenciesUseCaseErrorMapper {

    override fun mapError(error: Response.Error<SupportedTransitAgenciesData>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            ErrorCode.SupportedCities.SUPPORTED_CITIES_ERROR ->
                resources.getString(R.string.error_load_supported_cities)
            else -> error.rawErrorMessage
        }
    }
}