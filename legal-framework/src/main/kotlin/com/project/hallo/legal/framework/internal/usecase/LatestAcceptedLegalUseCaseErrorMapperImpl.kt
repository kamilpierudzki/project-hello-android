package com.project.hallo.legal.framework.internal.usecase

import android.content.res.Resources
import com.project.hallo.commons.domain.data.Response
import com.project.hallo.legal.framework.R
import com.project.hello.legal.domain.model.LegalErrorCode.LatestAcceptedLegal.LATEST_ACCEPTED_LEGAL_ERROR
import com.project.hello.legal.domain.usecase.LatestAcceptedLegalUseCaseErrorMapper
import javax.inject.Inject

internal class LatestAcceptedLegalUseCaseErrorMapperImpl @Inject constructor(
    private val resources: Resources
) : LatestAcceptedLegalUseCaseErrorMapper {

    override fun mapError(error: Response.Error<Int>) {
        error.localisedErrorMessage = when (error.rawErrorMessage) {
            LATEST_ACCEPTED_LEGAL_ERROR -> resources.getString(R.string.error_load_latest_accepted_legal)
            else -> error.rawErrorMessage
        }
    }
}