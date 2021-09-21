package com.project.hello.vehicle.prediction.framework.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.project.hello.vehicle.prediction.framework.internal.fps.implementation.FpsCounterManagerImpl
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class FpsCounterManagerImplTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    val tested = FpsCounterManagerImpl()

    @Test
    fun `test 1`() {
        // given
        var counter = 0
        tested.currentValue.observeForever { counter = it }

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_100)
        tested.newFrameProcessed(1_200)
        tested.newFrameProcessed(2_000)

        // then
        Assert.assertEquals(3, counter)
    }

    @Test
    fun `test 2`() {
        // given
        var counter = 0
        tested.currentValue.observeForever { counter = it }

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_500)
        tested.newFrameProcessed(2_000)
        tested.newFrameProcessed(2_500)

        // then
        Assert.assertEquals(2, counter)
    }

    @Test
    fun `test 3`() {
        // given
        val observer: Observer<Int> = mock()
        tested.currentValue.observeForever(observer)

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_100)
        tested.newFrameProcessed(1_200)
        tested.newFrameProcessed(2_000)

        // then
        verify(observer, times(1)).onChanged(3)
    }

    @Test
    fun `test 4`() {
        // given
        val observer: Observer<Int> = mock()
        tested.currentValue.observeForever(observer)

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_500)
        tested.newFrameProcessed(1_700)
        tested.newFrameProcessed(2_000)
        tested.newFrameProcessed(2_500)
        tested.newFrameProcessed(3_000)

        // then
        verify(observer, times(1)).onChanged(3)
        verify(observer, times(1)).onChanged(2)
    }
}