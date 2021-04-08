package com.project.halo.vehicle.domain.analysis.implementation

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType
import com.project.halo.vehicle.prediction.data.analysis.LineWithProbability
import org.junit.Assert
import org.junit.Test

internal class PredictedLinesAnalysisImplTest {

    val t = VehicleType.TRAM
    val line_A = Line("A", listOf("aaa"), t)
    val line_B = Line("B", listOf("bbb"), t)
    val line_C = Line("C", listOf("ccc"), t)

    val tested = PredictedLinesAnalysisImpl()

    @Test
    fun `test 1`() {
        // given
        val iteration0 = listOf(line_A)
        val iteration1 = listOf(line_A, line_B)
        val iteration2 = listOf(line_A, line_B, line_C)

        // when
        var analysed0: List<LineWithProbability>? = null
        tested.analysedLines(iteration0, 1_000) { analysed0 = it }
        var analysed1: List<LineWithProbability>? = null
        tested.analysedLines(iteration1, 1_001) { analysed1 = it }
        var analysed2: List<LineWithProbability>? = null
        tested.analysedLines(iteration2, 1_002) { analysed2 = it }

        // then
        Assert.assertNotNull(analysed0)
        Assert.assertEquals(1, analysed0!!.size)
        Assert.assertEquals(1.0f, analysed0!![0].probability)

        Assert.assertNotNull(analysed1)
        Assert.assertEquals(2, analysed1!!.size)
        Assert.assertEquals(0.6666667f, analysed1!![0].probability)
        Assert.assertEquals(0.33333334f, analysed1!![1].probability)

        Assert.assertNotNull(analysed2)
        Assert.assertEquals(3, analysed2!!.size)
        Assert.assertEquals(0.5f, analysed2!![0].probability)
        Assert.assertEquals(0.33333334f, analysed2!![1].probability)
        Assert.assertEquals(0.16666667f, analysed2!![2].probability)
    }

    @Test
    fun `test 2`() {
        // given
        val iteration0 = listOf(line_A)
        val iteration1 = listOf(line_A)
        val iteration2 = listOf(line_B)

        // when
        var analysed0: List<LineWithProbability>? = null
        tested.analysedLines(iteration0, 1_000) { analysed0 = it }
        var analysed1: List<LineWithProbability>? = null
        tested.analysedLines(iteration1, 2_000) { analysed1 = it }
        var analysed2: List<LineWithProbability>? = null
        tested.analysedLines(iteration2, 50_000) { analysed2 = it }

        // then
        Assert.assertNotNull(analysed0)
        Assert.assertNull(analysed1)
        Assert.assertNotNull(analysed2)
    }

    @Test
    fun `test 3`() {
        // given
        val iteration0 = listOf(line_A)
        val iteration1 = listOf(line_A)
        val iteration2 = listOf(line_A)

        // when
        var analysed0: List<LineWithProbability>? = null
        tested.analysedLines(iteration0, 1_000) { analysed0 = it }
        var analysed1: List<LineWithProbability>? = null
        tested.analysedLines(iteration1, 2_000) { analysed1 = it }
        var analysed2: List<LineWithProbability>? = null
        tested.analysedLines(iteration2, 50_000) { analysed2 = it }

        // then
        Assert.assertNotNull(analysed0)
        Assert.assertNull(analysed1)
        Assert.assertNull(analysed2)
    }
}