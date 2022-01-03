package com.project.hello.vehicle.prediction.analysis

import com.project.hello.transit.agency.model.Line
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class LineWithShareTest {

    val line = Line("1", "a")

    @Test
    fun test_1() {
        assertFailsWith(IllegalArgumentException::class) {
            LineWithShare(line, -1)
        }
    }

    @Test
    fun test_2() {
        val result = LineWithShare(line, 0)
        assertEquals(0, result.share)
    }

    @Test
    fun test_3() {
        val result = LineWithShare(line, 99)
        assertEquals(99, result.share)
    }

    @Test
    fun test_4() {
        assertFailsWith(IllegalArgumentException::class) {
            LineWithShare(line, 101)
        }
    }

    @Test
    fun test_5() {
        val result = LineWithShare(line, 50)
        assertEquals(50, result.share)
    }
}