package com.project.hello.vehicle.prediction.framework.internal.analysis

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.LineWithAccuracy
import com.project.hello.vehicle.prediction.framework.internal.analysis.PredictedLinesAnalysisImpl
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
}