package com.project.hello.legal.internal.usecase.implementation

import com.project.hello.commons.data.Response
import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.model.api.LatestAvailableLegalApi
import com.project.hello.legal.repository.LegalDataResource
import com.project.hello.legal.repository.LegalRepository
import com.project.hello.legal.usecase.LatestAvailableLegalUseCaseErrorMapper
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class LatestAvailableLegalUseCaseImplTest {

    val legalDataResource: LegalDataResource = mock()
    val legalRepository: LegalRepository = mock {
        on { getLegalDataResource() } doReturn legalDataResource
    }
    val latestAvailableLegalUseCaseErrorMapper: LatestAvailableLegalUseCaseErrorMapper = mock()

    val tested = LatestAvailableLegalUseCaseImpl(
        legalRepository,
        latestAvailableLegalUseCaseErrorMapper
    )

    @Test
    fun `given legal info is available when execute is called both loading event and successful event are sent`() {
        // given
        val legalApi = LatestAvailableLegalApi(1, "")
        whenever(legalDataResource.latestAvailableLegal()).thenReturn(
            ResponseApi.Success(legalApi)
        )

        // when
        val observer = tested.execute().test()

        // then
        observer.assertValueCount(2)
        observer.assertValueAt(0) { it is Response.Loading }
        observer.assertValueAt(1) { it is Response.Success }
    }

    @Test
    fun `given legal info is NOT available when execute is called both loading event and error event are sent`() {
        // given
        whenever(legalDataResource.latestAvailableLegal()).thenReturn(
            ResponseApi.Error("error")
        )

        // when
        val observer = tested.execute().test()

        // then
        observer.assertValueCount(2)
        observer.assertValueAt(0) { it is Response.Loading }
        observer.assertValueAt(1) { it is Response.Error }
    }
}