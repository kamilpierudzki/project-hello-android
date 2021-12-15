package com.project.hello.transit.agency.internal.usecase

import android.content.res.Resources
import com.project.hello.commons.data.Response
import com.project.hello.transit.agency.R
import com.project.hello.transit.agency.model.ErrorCode
import com.project.hello.transit.agency.model.TransitAgency
import com.project.hello.transit.agency.usecase.SelectedTransitAgencyUseCaseErrorMapper
import javax.inject.Inject

internal class SelectedTransitAgencyUseCaseErrorMapperImpl @Inject constructor(
    private val resources: Resources
) : SelectedTransitAgencyUseCaseErrorMapper {

    override fun mapError(error: Response.Error<TransitAgency>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            ErrorCode.SelectedCity.SELECTED_CITY_ERROR ->
                resources.getString(R.string.transit_agency_error_load_selected_transit_agency)
            else -> error.rawErrorMessage
        }
    }
}