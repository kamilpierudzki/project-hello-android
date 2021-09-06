package com.project.hello.vehicle.prediction.framework.internal.analysis

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.LineWithAccuracy
import org.junit.Assert
import org.junit.Test

internal class PredictedLinesAnalysisImplTest {

    val line_A = LineWithAccuracy(Line("A", "aaa"), AccuracyLevel.NUMBER_MATCHED)
    val line_B = LineWithAccuracy(Line("B", "bbb"), AccuracyLevel.NUMBER_MATCHED)
    val line_C = LineWithAccuracy(Line("C", "ccc"), AccuracyLevel.NUMBER_MATCHED)

    val tested = PredictedLinesAnalysisImpl()

    @Test
    fun `test 1`() {
        // given
        val iteration0 = listOf(line_A)
        val iteration1 = listOf(line_A, line_B)
        val iteration2 = listOf(line_A, line_B, line_C)

        // when
        val analysed0 = tested.analysedSortedLines(iteration0, 1_000)
        val analysed1 = tested.analysedSortedLines(iteration1, 1_001)
        val analysed2 = tested.analysedSortedLines(iteration2, 1_002)

        // then
        Assert.assertEquals(1, analysed0.size)
        Assert.assertEquals(1.0f, analysed0[0].probability)

        Assert.assertEquals(2, analysed1.size)
        Assert.assertEquals(0.6666667f, analysed1[0].probability)
        Assert.assertEquals(0.33333334f, analysed1[1].probability)

        Assert.assertEquals(3, analysed2.size)
        Assert.assertEquals(0.5f, analysed2[0].probability)
        Assert.assertEquals(0.33333334f, analysed2[1].probability)
        Assert.assertEquals(0.16666667f, analysed2[2].probability)
    }

    @Test
    fun `test 2`() {
        // given
        val line0 = LineWithAccuracy(Line("15", "A"), AccuracyLevel.NUMBER_MATCHED)
        val line1 = LineWithAccuracy(Line("5", "A"), AccuracyLevel.NUMBER_SLICE)
        val line2 = LineWithAccuracy(Line("15", "B"), AccuracyLevel.NUMBER_SLICE)
        val line3 = LineWithAccuracy(Line("5", "B"), AccuracyLevel.NUMBER_SLICE)
        val iteration0 = listOf(line0, line1, line2, line3)

        // when
        val analysed0 = tested.analysedSortedLines(iteration0, 1_000)

        // then
        Assert.assertEquals(4, analysed0.size)
        Assert.assertEquals("15", analysed0[0].line.number)
        Assert.assertEquals("5", analysed0[1].line.number)
        Assert.assertEquals("5", analysed0[2].line.number)
        Assert.assertEquals("15", analysed0[3].line.number)
    }

    @Test
    fun `test 3`() {
        // given
        val line0 = LineWithAccuracy(Line("15", "A"), AccuracyLevel.NUMBER_MATCHED)
        val line1 = LineWithAccuracy(Line("5", "A"), AccuracyLevel.DESTINATION_SLICE)
        val line2 = LineWithAccuracy(Line("5", "A"), AccuracyLevel.DESTINATION_SLICE)
        val line3 = LineWithAccuracy(Line("5", "A"), AccuracyLevel.DESTINATION_SLICE)
        val line4 = LineWithAccuracy(Line("5", "A"), AccuracyLevel.DESTINATION_SLICE)
        val line5 = LineWithAccuracy(Line("5", "A"), AccuracyLevel.DESTINATION_SLICE)
        val iteration0 = listOf(line0, line1, line2, line3, line4, line5)

        // when
        val analysed0 = tested.analysedSortedLines(iteration0, 1_000)

        // then
        Assert.assertEquals(2, analysed0.size)
        Assert.assertEquals("5", analysed0[0].line.number)
        Assert.assertEquals("15", analysed0[1].line.number)
    }
}