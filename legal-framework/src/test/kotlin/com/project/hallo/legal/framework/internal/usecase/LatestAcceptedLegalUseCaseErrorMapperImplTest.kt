package com.project.hallo.legal.framework.internal.usecase

import android.content.res.Resources
import com.project.hallo.commons.domain.data.Response
import com.project.hello.legal.domain.model.LegalErrorCode.LatestAcceptedLegal.LATEST_ACCEPTED_LEGAL_ERROR
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class LatestAcceptedLegalUseCaseErrorMapperImplTest {

    val translatedString = "translated"
    val resources: Resources = mock {
        on { getString(any()) } doReturn translatedString
    }

    val tested = LatestAcceptedLegalUseCaseErrorMapperImpl(resources)

    @Test
    fun `given raw error is LATEST_ACCEPTED_LEGAL_ERROR when mapError is called then error is translated`() {
        // given
        val error = Response.Error<Int>(LATEST_ACCEPTED_LEGAL_ERROR)

        // when
        tested.mapError(error)

        // then
        assert(error.localisedErrorMessage == translatedString)
    }

    @Test
    fun `given raw error is different than LATEST_ACCEPTED_LEGAL_ERROR when mapError is called then error is NOT translated`() {
        // given
        val rawError = "rawError"
        val error = Response.Error<Int>(rawError)

        // when
        tested.mapError(error)

        // then
        assert(error.localisedErrorMessage != translatedString)
        assert(error.localisedErrorMessage == rawError)
    }
}