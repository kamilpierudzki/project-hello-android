package com.project.hello.vehicle.prediction

import kotlin.test.Test
import kotlin.test.assertEquals

class FpsCounterTest {

    @Test
    fun test_1() {
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
        assertEquals(3, fps)
    }

    @Test
    fun test_2() {
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
        assertEquals(2, fps)
    }

    @Test
    fun test_3() {
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
        assertEquals(1, counter)
        assertEquals(3, fps)
    }

    @Test
    fun test_4() {
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
        assertEquals(2, fpses.size)
        assertEquals(3, fpses[0])
        assertEquals(2, fpses[1])
    }
}