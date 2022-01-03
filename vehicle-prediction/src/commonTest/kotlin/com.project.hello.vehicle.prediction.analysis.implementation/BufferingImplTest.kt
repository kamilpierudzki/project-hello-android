package com.project.hello.vehicle.prediction.analysis.implementation

import com.project.hello.transit.agency.model.Line
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BufferingImplTest {

    val tested = BufferingImpl(TestSynchronization())

    @Test
    fun test_1() {
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
        assertEquals("1", buffered0!!.line.number)
        assertEquals(null, buffered1)
        assertEquals(null, buffered2)
        assertEquals("0", buffered3!!.line.number)
        // 2nd cycle
        assertEquals("2", buffered4!!.line.number)
    }

    @Test
    fun test_2() {
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
        assertEquals("1", buffered0!!.line.number)
        assertEquals(null, buffered1)
        assertEquals(null, buffered2)
        assertEquals("0", buffered3!!.line.number)
        assertEquals("0", buffered4!!.line.number)
        // 2nd cycle
        assertEquals("2", buffered5!!.line.number)
    }

    @Test
    fun test_3() {
        // given
        val line0 = Line("0", "A")
        val line1 = Line("1", "B")

        // when
        val buffered0 = tested.bufferedLine(1_001, line1)
        val buffered1 = tested.bufferedLine(1_002, line0)
        val buffered2 = tested.bufferedLine(1_003, line1)
        val buffered3 = tested.bufferedLine(1_004, line0)

        // then
        assertEquals("1", buffered0!!.line.number)
        assertEquals(null, buffered1)
        assertEquals("1", buffered2!!.line.number)
        assertEquals(null, buffered3)
    }

    @Test
    fun test_4() {
        // given
        val line0 = Line("0", "A")

        // when
        // 1st cycle
        val buffered0 = tested.bufferedLine(0, line0)
        val buffered1 = tested.bufferedLine(0, line0)

        // then
        assertEquals("0", buffered0!!.line.number)
        assertEquals("0", buffered1!!.line.number)
    }

    @Test
    fun test_5() {
        // given
        val line0 = Line("16", "Os. Sobieskiego")

        // when
        val buffered0 = tested.bufferedLine(1632951714824, line0)
        val buffered1 = tested.bufferedLine(1632951714888, line0)
        val buffered2 = tested.bufferedLine(1632951715026, line0)

        // then
        assertEquals("16", buffered0!!.line.number)
        assertEquals("16", buffered1!!.line.number)
        assertEquals("16", buffered2!!.line.number)
    }
}