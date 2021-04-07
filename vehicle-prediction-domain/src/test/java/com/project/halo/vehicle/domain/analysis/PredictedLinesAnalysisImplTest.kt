package com.project.halo.vehicle.domain.analysis

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType
import org.junit.Assert
import org.junit.Test

internal class PredictedLinesAnalysisImplTest {

    val tram = VehicleType.TRAM
    val line_12 = Line("12", listOf("aaa"), tram)
    val line_16 = Line("16", listOf("bbb"), tram)
    val line_1 = Line("1", listOf("ccc"), tram)

    val tested = PredictedLinesAnalysisImpl()

    @Test
    fun `test 1`() {
        // given
        val lines1 = listOf(line_12, line_16)
        val lines2 = listOf(line_12, line_1)

        // when
        var analysed0: List<Line>? = null
        tested.analysedLines(lines1, 1_000) { analysed0 = it }
        var analysed1: List<Line>? = null
        tested.analysedLines(lines2, 2_000) { analysed1 = it }

        // then
        Assert.assertNotNull(analysed0)
        Assert.assertNotNull(analysed1)
        Assert.assertEquals(3, analysed1!!.size)
        Assert.assertTrue("12" == analysed1!![0].number || "1" == analysed1!![0].number)
        Assert.assertTrue("12" == analysed1!![1].number || "1" == analysed1!![1].number)
        Assert.assertEquals("16", analysed1!![2].number)
    }

    @Test
    fun `test 2`() {
        // given
        val lines1 = listOf(line_12, line_16)
        val lines2 = listOf(line_1)

        // when
        tested.analysedLines(lines1, 1_000) {}
        tested.analysedLines(lines1, 12_000) {}
        var analysed: List<Line>? = null
        tested.analysedLines(lines2, 15_000) { analysed = it }

        // then
        Assert.assertNotNull(analysed)
        Assert.assertEquals(1, analysed!!.size)
        Assert.assertEquals("1", analysed!![0].number)
    }

    @Test
    fun `test 3`() {
        // given
        val lines1 = listOf(line_12)

        // when
        tested.analysedLines(lines1, 1_000) {}
        tested.analysedLines(lines1, 11_000) {}
        var analysed4: List<Line>? = null
        tested.analysedLines(lines1, 13_000) { analysed4 = it }

        // then
        Assert.assertNotNull(analysed4)
        Assert.assertEquals(1, analysed4!!.size)
        Assert.assertEquals("12", analysed4!![0].number)
    }

    @Test
    fun `test 4`() {
        // given
        val lines1 = listOf(line_12)

        // when
        var analysed0: List<Line>? = null
        tested.analysedLines(lines1, 1_000) { analysed0 = it }
        var analysed1: List<Line>? = null
        tested.analysedLines(lines1, 2_000) { analysed1 = it }
        var analysed2: List<Line>? = null
        tested.analysedLines(lines1, 3_000) { analysed2 = it }

        // then
        Assert.assertNotNull(analysed0)
        Assert.assertNull(analysed1)
        Assert.assertNull(analysed2)
        Assert.assertEquals(1, analysed0!!.size)
        Assert.assertEquals("12", analysed0!![0].number)
    }

    @Test
    fun `test 5`() {
        // given
        val lines1 = listOf(line_12)

        // when
        var analysed0: List<Line>? = null
        tested.analysedLines(lines1, 1_000) { analysed0 = it }
        var analysed1: List<Line>? = null
        tested.analysedLines(lines1, 11_000) { analysed1 = it }
        var analysed2: List<Line>? = null
        tested.analysedLines(lines1, 12_000) { analysed2 = it }
        var analysed3: List<Line>? = null
        tested.analysedLines(lines1, 12_000) { analysed3 = it }

        // then
        Assert.assertNotNull(analysed0)
        Assert.assertNotNull(analysed1)
        Assert.assertNotNull(analysed2)
        Assert.assertNull(analysed3)
        Assert.assertEquals(1, analysed0!!.size)
        Assert.assertEquals("12", analysed0!![0].number)
        Assert.assertEquals(0, analysed1!!.size)
        Assert.assertEquals(1, analysed2!!.size)
        Assert.assertEquals("12", analysed2!![0].number)
    }
}