package com.project.hello.legal.framework.usecase

import android.content.res.Resources
import com.project.hello.commons.domain.data.Response
import com.project.hello.legal.domain.model.LatestAvailableLegal
import com.project.hello.legal.domain.model.LegalErrorCode.LatestAvailableLegal.LATEST_AVAILABLE_LEGAL_ERROR
import com.project.hello.legal.framework.internal.usecase.LatestAvailableLegalUseCaseErrorMapperImpl
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class LatestAvailableLegalUseCaseErrorMapperImplTest {

    val translatedString = "translated"
    val resources: Resources = mock {
        on { getString(any()) } doReturn translatedString
    }

    val tested = LatestAvailableLegalUseCaseErrorMapperImpl(resources)

    @Test
    fun `given raw error is LATEST_AVAILABLE_LEGAL_ERROR when mapError is called then error is translated`() {
        // given
        val error = Response.Error<LatestAvailableLegal>(LATEST_AVAILABLE_LEGAL_ERROR)

        // when
        tested.mapError(error)

        // then
        assert(error.localisedErrorMessage == translatedString)
    }

    @Test
    fun `given raw error is different than LATEST_AVAILABLE_LEGAL_ERROR when mapError is called then error is NOT translated`() {
        // given
        val rawError = "rawError"
        val error = Response.Error<LatestAvailableLegal>(rawError)

        // when
        tested.mapError(error)

        // then
        assert(error.localisedErrorMessage != translatedString)
        assert(error.localisedErrorMessage == rawError)
    }
}