package com.project.hello.city.plan.framework.internal.usecase

import android.content.res.Resources
import com.project.hello.city.plan.framework.R
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.domain.model.ErrorCode
import com.project.hello.transit.agency.domain.usecase.SelectedTransitAgencyUseCaseErrorMapper
import com.project.hello.commons.domain.data.Response
import javax.inject.Inject

internal class SelectedTransitAgencyUseCaseErrorMapperImpl @Inject constructor(
    private val resources: Resources
) : SelectedTransitAgencyUseCaseErrorMapper {

    override fun mapError(error: Response.Error<TransitAgency>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            ErrorCode.SelectedCity.SELECTED_CITY_ERROR ->
                resources.getString(R.string.error_load_selected_city)
            else -> error.rawErrorMessage
        }
    }
}