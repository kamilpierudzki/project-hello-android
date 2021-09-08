package com.project.hello.vehicle.domain.analysis.implementation

import com.project.hello.city.plan.domain.model.Line
import org.junit.Assert
import org.junit.Test

internal class PredictedLinesAnalysisImplTest {

    val line_A = Line("A", "aaa")
    val line_B = Line("B", "bbb")
    val line_C = Line("C", "ccc")

    val tested = PredictedLinesAnalysisImpl()

    @Test
    fun `test 1`() {
        // given
        val iteration0 = 1_000L
        val iteration1 = 1_001L
        val iteration2 = 1_002L

        // when
        val analysed0 = tested.bufferedLine(iteration0, line_A)

        val analysed1 = tested.bufferedLine(iteration1, line_A)
        val analysed2 = tested.bufferedLine(iteration1, line_B)

        val analysed3 = tested.bufferedLine(iteration2, line_A)
        val analysed4 = tested.bufferedLine(iteration2, line_B)
        val analysed5 = tested.bufferedLine(iteration2, line_C)

        // then
        Assert.assertEquals(analysed0!!.line, line_A)
        Assert.assertEquals(analysed1!!.line, line_A)
        Assert.assertEquals(analysed2!!.line, line_A)
        Assert.assertEquals(analysed3!!.line, line_A)
        Assert.assertEquals(analysed4!!.line, line_A)
        Assert.assertEquals(analysed5!!.line, line_A)
    }

    @Test
    fun `test 2`() {
        // given
        val line0 = Line("15", "A")
        val line1 = Line("5", "A")
        val line2 = Line("15", "B")
        val line3 = Line("5", "B")
        val iteration0 = 1_000L
        val iteration1 = 1_001L
        val iteration2 = 1_002L
        val iteration3 = 4_000L

        // when
        val analysed0 = tested.bufferedLine(iteration0, line0)
        val analysed1 = tested.bufferedLine(iteration1, line1)
        val analysed2 = tested.bufferedLine(iteration2, line2)
        val analysed3 = tested.bufferedLine(iteration3, line3)

        // then
        Assert.assertEquals("15", analysed0!!.line.number)
        Assert.assertEquals("15", analysed1!!.line.number)
        Assert.assertEquals("15", analysed2!!.line.number)
        Assert.assertEquals("5", analysed3!!.line.number)
    }
}