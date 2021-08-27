package com.project.hello.city.plan.domain.usecase.implementation

import android.content.res.Resources
import com.project.hello.city.plan.domain.R
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.domain.model.ErrorCode
import com.project.hello.city.plan.domain.usecase.SelectedCityUseCaseErrorMapper
import com.project.hello.commons.domain.data.Response

class SelectedCityUseCaseErrorMapperImpl(private val resources: Resources) :
    SelectedCityUseCaseErrorMapper {

    override fun mapError(error: Response.Error<CityPlan>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            ErrorCode.SelectedCity.SELECTED_CITY_ERROR ->
                resources.getString(R.string.error_load_selected_city)
            else -> error.rawErrorMessage
        }
    }
}