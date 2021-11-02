package com.project.hello.vehicle.domain.analysis

import com.project.hello.transit.agency.domain.model.Line
import org.junit.Assert
import org.junit.Test

internal class LineWithShareTest {

    val line = Line("1", "a")

    @Test
    fun `test 1`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
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
        Assert.assertThrows(IllegalArgumentException::class.java) {
            LineWithShare(line, 101)
        }
    }

    @Test
    fun `test 5`() {
        val result = LineWithShare(line, 50)
        Assert.assertEquals(50, result.share)
    }
}