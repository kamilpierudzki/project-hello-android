package com.project.hello.vehicle.domain

import org.junit.Assert
import org.junit.Test

class FpsCounterTest {

    @Test
    fun `test 1`() {
        // given
        var fps = 0
        val callback: (Int) -> Unit = { fps = it }
        val tested = FpsCounter(callback)

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_100)
        tested.newFrameProcessed(1_200)
        tested.newFrameProcessed(2_000)

        // then
        Assert.assertEquals(3, fps)
    }

    @Test
    fun `test 2`() {
        // given
        var fps = 0
        val callback: (Int) -> Unit = { fps = it }
        val tested = FpsCounter(callback)

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_500)
        tested.newFrameProcessed(2_000)
        tested.newFrameProcessed(2_500)

        // then
        Assert.assertEquals(2, fps)
    }

    @Test
    fun `test 3`() {
        // given
        var counter = 0
        var fps = -1
        val callback: (Int) -> Unit = {
            fps = it
            counter++
        }
        val tested = FpsCounter(callback)

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_100)
        tested.newFrameProcessed(1_200)
        tested.newFrameProcessed(2_000)

        // then
        Assert.assertEquals(1, counter)
        Assert.assertEquals(3, fps)
    }

    @Test
    fun `test 4`() {
        // given
        val fpses = mutableListOf<Int>()
        val callback: (Int) -> Unit = { fpses.add(it) }
        val tested = FpsCounter(callback)

        // when
        tested.newFrameProcessed(1_000)
        tested.newFrameProcessed(1_500)
        tested.newFrameProcessed(1_700)
        tested.newFrameProcessed(2_000)
        tested.newFrameProcessed(2_500)
        tested.newFrameProcessed(3_000)

        // then
        Assert.assertEquals(2, fpses.size)
        Assert.assertEquals(3, fpses[0])
        Assert.assertEquals(2, fpses[1])
    }
}