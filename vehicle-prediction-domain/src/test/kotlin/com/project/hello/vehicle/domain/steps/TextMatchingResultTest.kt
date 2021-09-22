package com.project.hello.vehicle.domain.steps

import org.junit.Assert
import org.junit.Test

class TextMatchingResultTest {

    @Test
    fun `test 1`() {
        Assert.assertThrows(IllegalStateException::class.java) {
            TextMatchingResult.Positive(-1)
        }
    }

    @Test
    fun `test 2`() {
        val result = TextMatchingResult.Positive(0)
        Assert.assertEquals(0, result.percentage)
    }

    @Test
    fun `test 3`() {
        val result = TextMatchingResult.Positive(99)
        Assert.assertEquals(99, result.percentage)
    }

    @Test
    fun `test 4`() {
        Assert.assertThrows(IllegalStateException::class.java) {
            TextMatchingResult.Positive(101)
        }
    }
}