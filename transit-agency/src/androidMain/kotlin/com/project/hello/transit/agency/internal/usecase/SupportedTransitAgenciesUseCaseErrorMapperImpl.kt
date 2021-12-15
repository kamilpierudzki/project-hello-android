package com.project.hello.transit.agency.internal.usecase

import android.content.res.Resources
import com.project.hello.commons.data.Response
import com.project.hello.transit.agency.R
import com.project.hello.transit.agency.model.ErrorCode
import com.project.hello.transit.agency.model.SupportedTransitAgenciesData
import com.project.hello.transit.agency.usecase.SupportedTransitAgenciesUseCaseErrorMapper
import javax.inject.Inject

internal class SupportedTransitAgenciesUseCaseErrorMapperImpl @Inject constructor(
    private val resources: Resources
) : SupportedTransitAgenciesUseCaseErrorMapper {

    override fun mapError(error: Response.Error<SupportedTransitAgenciesData>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            ErrorCode.SupportedCities.SUPPORTED_CITIES_ERROR ->
                resources.getString(R.string.transit_agency_error_load_supported_transit_agencies)
            else -> error.rawErrorMessage
        }
    }
}