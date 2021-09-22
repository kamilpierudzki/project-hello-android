package com.project.hello.vehicle.domain.analysis

import com.project.hello.city.plan.domain.model.Line
import org.junit.Assert
import org.junit.Test

internal class LineWithProbabilityTest {

    val line = Line("1", "a")

    @Test
    fun `test 1`() {
        Assert.assertThrows(IllegalStateException::class.java) {
            LineWithProbability(line, -1)
        }
    }

    @Test
    fun `test 2`() {
        val result = LineWithProbability(line, 0)
        Assert.assertEquals(0, result.probability)
    }

    @Test
    fun `test 3`() {
        val result = LineWithProbability(line, 99)
        Assert.assertEquals(99, result.probability)
    }

    @Test
    fun `test 4`() {
        Assert.assertThrows(IllegalStateException::class.java) {
            LineWithProbability(line, 101)
        }
    }
}