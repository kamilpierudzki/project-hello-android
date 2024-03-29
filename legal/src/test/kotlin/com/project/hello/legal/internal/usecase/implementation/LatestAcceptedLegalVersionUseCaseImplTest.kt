package com.project.hello.legal.internal.usecase.implementation

import com.project.hello.commons.data.Response
import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.repository.LegalDataResource
import com.project.hello.legal.repository.LegalRepository
import com.project.hello.legal.usecase.LatestAcceptedLegalUseCaseErrorMapper
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class LatestAcceptedLegalVersionUseCaseImplTest {

    val legalDataResource: LegalDataResource = mock()
    val legalRepository: LegalRepository = mock {
        on { getLegalDataResource() } doReturn legalDataResource
    }
    val latestAcceptedLegalUseCaseErrorMapper: LatestAcceptedLegalUseCaseErrorMapper = mock()

    val tested = LatestAcceptedLegalVersionUseCaseImpl(
        legalRepository,
        latestAcceptedLegalUseCaseErrorMapper
    )

    @Test
    fun `given latest accepted version is saved when execute is called then both loading event and successful event are sent`() {
        // given
        whenever(legalDataResource.latestAcceptedLegalVersion()).thenReturn(ResponseApi.Success(1))

        // when
        val observer = tested.execute().test()

        // then
        observer.assertValueCount(2)
        observer.assertValueAt(0) { it is Response.Loading }
        observer.assertValueAt(1) { it is Response.Success }
    }

    @Test
    fun `given latest accepted version is NOT saved when execute is called then both loading event and error event are sent`() {
        // given
        whenever(legalDataResource.latestAcceptedLegalVersion()).thenReturn(ResponseApi.Error(""))

        // when
        val observer = tested.execute().test()

        // then
        observer.assertValueCount(2)
        observer.assertValueAt(0) { it is Response.Loading }
        observer.assertValueAt(1) { it is Response.Error }
    }
}