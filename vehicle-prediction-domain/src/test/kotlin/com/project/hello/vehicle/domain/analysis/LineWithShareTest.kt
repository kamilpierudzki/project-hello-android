package com.project.hello.vehicle.domain.analysis

import com.project.hello.city.plan.domain.model.Line
import org.junit.Assert
import org.junit.Test

internal class LineWithShareTest {

    val line = Line("1", "a")

    @Test
    fun `test 1`() {
        Assert.assertThrows(IllegalStateException::class.java) {
            LineWithShare(line, -1)
        }
    }

    @Test
    fun `test 2`() {
        val result = LineWithShare(line, 0)
        Assert.assertEquals(0, result.share)
    }

    @Test
    fun `test 3`() {
        val result = LineWithShare(line, 99)
        Assert.assertEquals(99, result.share)
    }

    @Test
    fun `test 4`() {
        Assert.assertThrows(IllegalStateException::class.java) {
            LineWithShare(line, 101)
        }
    }
}