package com.project.hello.legal.internal.usecase.implementation

import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.model.LatestAvailableLegal
import com.project.hello.legal.repository.LegalDataResource
import com.project.hello.legal.repository.LegalRepository
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class LatestAvailableLegalSaverUseCaseImplTest {

    val legalDataResource: LegalDataResource = mock()
    val legalRepository: LegalRepository = mock {
        on { getLegalDataResource() } doReturn legalDataResource
    }

    val tested = LatestAvailableLegalSaverUseCaseImpl(legalRepository)

    @Test
    fun `given successful save of legal when execute is called then observable is completed`() {
        // given
        val legal = LatestAvailableLegal(1, "")
        whenever(legalDataResource.saveLatestAcceptedLegal(any())).thenReturn(
            ResponseApi.Success(Unit)
        )

        // when
        val observer = tested.execute(legal).test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `given failed save of legal when execute is called then observer received error`() {
        // given
        val legal = LatestAvailableLegal(1, "")
        whenever(legalDataResource.saveLatestAcceptedLegal(any())).thenReturn(
            ResponseApi.Error("error")
        )

        // when
        val observer = tested.execute(legal).test()

        // then
        observer.assertError(IllegalStateException::class.java)
    }
}