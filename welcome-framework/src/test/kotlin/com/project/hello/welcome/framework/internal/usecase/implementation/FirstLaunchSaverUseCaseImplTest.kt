package com.project.hello.welcome.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.welcome.domain.repository.WelcomeRepository
import com.project.hello.welcome.domain.repository.WelcomeResource
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class FirstLaunchSaverUseCaseImplTest {

    val welcomeResource: WelcomeResource = mock()
    val welcomeRepository: WelcomeRepository = mock {
        on { getWelcomeResource() } doReturn welcomeResource
    }

    val tested = FirstLaunchSaverUseCaseImpl(welcomeRepository)

    @Test
    fun `given first launch saving succeeds when execute is called then complete event is sent`() {
        // given
        whenever(welcomeResource.saveFirstLaunch()).thenReturn(ResponseApi.Success(Unit))

        // when
        val observer = tested.execute().test()

        // then
        observer.assertComplete()
    }

    @Test
    fun `given first launch saving fails when execute is called then error event is sent`() {
        // given
        whenever(welcomeResource.saveFirstLaunch()).thenReturn(ResponseApi.Error(""))

        // when
        val observer = tested.execute().test()

        // then
        observer.assertError { it is IllegalStateException }
    }
}