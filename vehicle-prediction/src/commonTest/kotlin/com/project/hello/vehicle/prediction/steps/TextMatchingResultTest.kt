package com.project.hello.vehicle.prediction.steps

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TextMatchingResultTest {

    @Test
    fun test_1() {
        assertFailsWith(IllegalArgumentException::class) {
            TextMatchingResult.Positive(-1)
        }
    }

    @Test
    fun test_2() {
        val result = TextMatchingResult.Positive(0)
        assertEquals(0, result.percentage)
    }

    @Test
    fun test_3() {
        val result = TextMatchingResult.Positive(99)
        assertEquals(99, result.percentage)
    }

    @Test
    fun test_4() {
        assertFailsWith(IllegalArgumentException::class) {
            TextMatchingResult.Positive(101)
        }
    }

    @Test
    fun test_5() {
        val result = TextMatchingResult.Positive(50)
        assertEquals(50, result.percentage)
    }
}