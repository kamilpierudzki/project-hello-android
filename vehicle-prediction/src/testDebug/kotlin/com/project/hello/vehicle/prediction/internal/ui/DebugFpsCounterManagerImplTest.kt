package com.project.hello.vehicle.prediction.internal.ui

import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hello.commons.test.TestLifecycle
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

internal class DebugFpsCounterManagerImplTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    val lifecycleOwner = TestLifecycle.create()

    val tested = DebugFpsCounterManagerImpl()

    @Test
    fun `test 1`() {
        // given
        val label: TextView = mock()
        tested.observeFpsCounterUiChanges(lifecycleOwner, label)
        lifecycleOwner.start()

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_100)
        tested.newFrameProcessed(1_200)
        tested.newFrameProcessed(2_000)

        // then
        verify(label, times(2)).text = anyString()
    }

    @Test
    fun `test 2`() {
        // given
        val label: TextView = mock()
        tested.observeFpsCounterUiChanges(lifecycleOwner, label)
        lifecycleOwner.start()

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_500)
        tested.newFrameProcessed(2_000)
        tested.newFrameProcessed(2_500)

        // then
        verify(label, times(2)).text = anyString()
    }

    @Test
    fun `test 3`() {
        // given
        val label: TextView = mock()
        tested.observeFpsCounterUiChanges(lifecycleOwner, label)
        lifecycleOwner.start()

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_100)
        tested.newFrameProcessed(1_200)
        tested.newFrameProcessed(2_000)

        // then
        verify(label, times(1)).text = "3"
    }

    @Test
    fun `test 4`() {
        // given
        val label: TextView = mock()
        tested.observeFpsCounterUiChanges(lifecycleOwner, label)
        lifecycleOwner.start()

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_500)
        tested.newFrameProcessed(1_700)
        tested.newFrameProcessed(2_000)
        tested.newFrameProcessed(2_500)
        tested.newFrameProcessed(3_000)

        // then
        verify(label, times(1)).text = "3"
        verify(label, times(1)).text = "2"
    }
}