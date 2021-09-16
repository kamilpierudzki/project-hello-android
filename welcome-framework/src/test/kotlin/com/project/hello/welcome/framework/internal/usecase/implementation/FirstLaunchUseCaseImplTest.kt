package com.project.hello.welcome.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.welcome.domain.repository.WelcomeRepository
import com.project.hello.welcome.domain.repository.WelcomeResource
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class FirstLaunchUseCaseImplTest {

    val welcomeResource: WelcomeResource = mock()
    val welcomeRepository: WelcomeRepository = mock {
        on { getWelcomeResource() } doReturn welcomeResource
    }

    val tested = FirstLaunchUseCaseImpl(welcomeRepository)

    @Test
    fun `given first launch already was when execute is called then negative event is sent`() {
        // given
        whenever(welcomeResource.isFirstLaunch()).thenReturn(ResponseApi.Success(false))

        // when
        val observer = tested.execute().test()

        // then
        observer.assertValueCount(1)
        observer.assertValueAt(0) { it == false }
    }

    @Test
    fun `given first launch has not already been when execute is called then positive event is sent`() {
        // given
        whenever(welcomeResource.isFirstLaunch()).thenReturn(ResponseApi.Success(true))

        // when
        val observer = tested.execute().test()

        // then
        observer.assertValueCount(1)
        observer.assertValueAt(0) { it == true }
    }

    @Test
    fun `given first launch returns error when execute is called then positive event is sent`() {
        // given
        whenever(welcomeResource.isFirstLaunch()).thenReturn(ResponseApi.Error(""))

        // when
        val observer = tested.execute().test()

        // then
        observer.assertError { it is IllegalStateException }
    }
}