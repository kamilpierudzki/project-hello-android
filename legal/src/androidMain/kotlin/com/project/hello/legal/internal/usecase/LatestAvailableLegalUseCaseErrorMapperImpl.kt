package com.project.hello.legal.internal.usecase

import android.content.res.Resources
import com.project.hello.commons.data.Response
import com.project.hello.legal.R
import com.project.hello.legal.model.LatestAvailableLegal
import com.project.hello.legal.model.LegalErrorCode.LatestAvailableLegal.LATEST_AVAILABLE_LEGAL_ERROR
import com.project.hello.legal.usecase.LatestAvailableLegalUseCaseErrorMapper
import javax.inject.Inject

internal class LatestAvailableLegalUseCaseErrorMapperImpl @Inject constructor(
    private val resources: Resources
) : LatestAvailableLegalUseCaseErrorMapper {

    override fun mapError(error: Response.Error<LatestAvailableLegal>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            LATEST_AVAILABLE_LEGAL_ERROR -> resources.getString(R.string.legal_error_load_latest_available_legal)
            else -> error.rawErrorMessage
        }
    }
}