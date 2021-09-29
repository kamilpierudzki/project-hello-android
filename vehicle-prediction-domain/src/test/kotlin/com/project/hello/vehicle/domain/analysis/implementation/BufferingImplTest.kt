package com.project.hello.vehicle.domain.analysis.implementation

import com.project.hello.city.plan.domain.model.Line
import org.junit.Assert
import org.junit.Test

internal class BufferingImplTest {

    val tested = BufferingImpl()

    @Test
    fun `test 1`() {
        // given
        val line0 = Line("0", "A")
        val line1 = Line("1", "B")
        val line2 = Line("2", "C")
        val line3 = Line("3", "D")

        // when
        // 1st cycle
        val buffered0 = tested.bufferedLine(1_001, line1)
        val buffered1 = tested.bufferedLine(1_002, line0)
        val buffered2 = tested.bufferedLine(1_003, line3)
        val buffered3 = tested.bufferedLine(1_004, line0)

        // 2nd cycle
        val buffered4 = tested.bufferedLine(5_000L, line2)

        // then
        // 1st cycle
        Assert.assertEquals("1", buffered0!!.line.number)
        Assert.assertEquals(null, buffered1)
        Assert.assertEquals(null, buffered2)
        Assert.assertEquals("0", buffered3!!.line.number)
        // 2nd cycle
        Assert.assertEquals("2", buffered4!!.line.number)
    }

    @Test
    fun `test 2`() {
        // given
        val line0 = Line("0", "A")
        val line1 = Line("1", "B")
        val line2 = Line("2", "C")

        // when
        // 1st cycle
        val buffered0 = tested.bufferedLine(1_001, line1)
        val buffered1 = tested.bufferedLine(1_002, line0)
        val buffered2 = tested.bufferedLine(1_003, null)
        val buffered3 = tested.bufferedLine(1_004, line0)
        val buffered4 = tested.bufferedLine(1_005, null)

        // 2nd cycle
        val buffered5 = tested.bufferedLine(5_000L, line2)

        // then
        // 1st cycle
        Assert.assertEquals("1", buffered0!!.line.number)
        Assert.assertEquals(null, buffered1)
        Assert.assertEquals(null, buffered2)
        Assert.assertEquals("0", buffered3!!.line.number)
        Assert.assertEquals("0", buffered4!!.line.number)
        // 2nd cycle
        Assert.assertEquals("2", buffered5!!.line.number)
    }

    @Test
    fun `test 3`() {
        // given
        val line0 = Line("0", "A")
        val line1 = Line("1", "B")

        // when
        val buffered0 = tested.bufferedLine(1_001, line1)
        val buffered1 = tested.bufferedLine(1_002, line0)
        val buffered2 = tested.bufferedLine(1_003, line1)
        val buffered3 = tested.bufferedLine(1_004, line0)

        // then
        Assert.assertEquals("1", buffered0!!.line.number)
        Assert.assertEquals(null, buffered1)
        Assert.assertEquals("1", buffered2!!.line.number)
        Assert.assertEquals(null, buffered3)
    }

    @Test
    fun `test 4`() {
        // given
        val line0 = Line("0", "A")

        // when
        // 1st cycle
        val buffered0 = tested.bufferedLine(0, line0)
        val buffered1 = tested.bufferedLine(0, line0)

        // then
        Assert.assertEquals("0", buffered0!!.line.number)
        Assert.assertEquals("0", buffered1!!.line.number)
    }

    @Test
    fun `test 5`() {
        // given
        val line0 = Line("16", "Os. Sobieskiego")

        // when
        val buffered0 = tested.bufferedLine(1632951714824, line0)
        val buffered1 = tested.bufferedLine(1632951714888, line0)
        val buffered2 = tested.bufferedLine(1632951715026, line0)

        // then
        Assert.assertEquals("16", buffered0!!.line.number)
        Assert.assertEquals("16", buffered1!!.line.number)
        Assert.assertEquals("16", buffered2!!.line.number)
    }
}