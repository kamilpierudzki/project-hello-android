package com.project.hello.welcome.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hello.commons.livedata.Event
import com.project.hello.commons.rx.ReactiveXUtils
import com.project.hello.welcome.internal.usecase.FirstLaunchSaverUseCase
import com.project.hello.welcome.internal.usecase.FirstLaunchUseCase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class WelcomeViewModelImplTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = ReactiveXUtils.getTestRule()

    val firstLaunchUseCase: FirstLaunchUseCase = mock {
        on { execute() } doReturn Single.just(true)
    }
    val firstLaunchSaverUseCase: FirstLaunchSaverUseCase = mock()

    @Test
    fun `given observing isFirstLaunch, FirstLaunchUseCase returns negative result when checkIFFirstLaunch is called then event is sent accordingly`() {
        // given
        whenever(firstLaunchUseCase.execute()).thenReturn(Single.just(false))

        val tested = tested()

        val events = mutableListOf<Event<Boolean>>()
        tested.isFirstLaunch.observeForever { events.add(it) }

        // when
        events.clear()
        tested.checkIFFirstLaunch()

        // then
        Assert.assertEquals(1, events.size)
        Assert.assertEquals(false, events[0].content)
    }

    @Test
    fun `given observing isFirstLaunch, FirstLaunchUseCase returns positive result when checkIFFirstLaunch is called then event is sent accordingly`() {
        // given
        whenever(firstLaunchUseCase.execute()).thenReturn(Single.just(true))

        val tested = tested()

        val events = mutableListOf<Event<Boolean>>()
        tested.isFirstLaunch.observeForever { events.add(it) }

        // when
        events.clear()
        tested.checkIFFirstLaunch()

        // then
        Assert.assertEquals(1, events.size)
        Assert.assertEquals(true, events[0].content)
    }

    @Test
    fun `given observing isFirstLaunch, FirstLaunchUseCase returns error result when checkIFFirstLaunch is called then event is sent accordingly`() {
        // given
        whenever(firstLaunchUseCase.execute()).thenReturn(Single.error(IllegalStateException()))

        val tested = tested()

        val events = mutableListOf<Event<Boolean>>()
        tested.isFirstLaunch.observeForever { events.add(it) }

        // when
        events.clear()
        tested.checkIFFirstLaunch()

        // then
        Assert.assertEquals(1, events.size)
        Assert.assertEquals(true, events[0].content)
    }

    @Test
    fun `given observing isFirstLaunch, FirstLaunchSaverUseCase returns result when markFirstLaunchAccomplished is called then event is sent accordingly`() {
        // given
        whenever(firstLaunchSaverUseCase.execute()).thenReturn(Completable.complete())

        val tested = tested()

        val events = mutableListOf<Event<Boolean>>()
        tested.isFirstLaunch.observeForever { events.add(it) }

        // when
        events.clear()
        tested.markFirstLaunchAccomplished()

        // then
        Assert.assertEquals(1, events.size)
        Assert.assertEquals(false, events[0].content)
    }

    @Test
    fun `given observing isFirstLaunch, FirstLaunchSaverUseCase returns error when markFirstLaunchAccomplished is called then event is sent accordingly`() {
        // given
        whenever(firstLaunchSaverUseCase.execute()).thenReturn(
            Completable.error(
                IllegalStateException()
            )
        )

        val tested = tested()

        val events = mutableListOf<Event<Boolean>>()
        tested.isFirstLaunch.observeForever { events.add(it) }

        // when
        events.clear()
        tested.markFirstLaunchAccomplished()

        // then
        Assert.assertEquals(1, events.size)
        Assert.assertEquals(true, events[0].content)
    }

    fun tested(
        firstLaunchUseCase: FirstLaunchUseCase = this.firstLaunchUseCase,
        firstLaunchSaverUseCase: FirstLaunchSaverUseCase = this.firstLaunchSaverUseCase
    ) = WelcomeViewModelImpl(firstLaunchUseCase, firstLaunchSaverUseCase)
}
