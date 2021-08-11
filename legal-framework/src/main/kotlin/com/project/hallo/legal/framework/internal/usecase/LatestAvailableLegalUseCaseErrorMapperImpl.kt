package com.project.hallo.legal.framework.internal.usecase

import android.content.res.Resources
import com.project.hallo.commons.domain.repository.Response
import com.project.hallo.legal.framework.R
import com.project.hello.legal.domain.model.LatestAvailableLegal
import com.project.hello.legal.domain.model.LegalErrorCode.LatestAvailableLegal.LATEST_AVAILABLE_LEGAL_ERROR
import com.project.hello.legal.domain.usecase.LatestAvailableLegalUseCaseErrorMapper
import javax.inject.Inject

internal class LatestAvailableLegalUseCaseErrorMapperImpl @Inject constructor(
    private val resources: Resources
) : LatestAvailableLegalUseCaseErrorMapper {

    override fun mapError(error: Response.Error<LatestAvailableLegal>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            LATEST_AVAILABLE_LEGAL_ERROR -> resources.getString(R.string.error_load_latest_available_legal)
            else -> error.rawErrorMessage
        }
    }
}