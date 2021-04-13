package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.vehicle.domain.steps.AccuracyLevel
import com.project.hallo.vehicle.domain.steps.LineWithAccuracy
import org.junit.Assert
import org.junit.Test

internal class OutputAnalysisImplTest {

    val tested = OutputAnalysisImpl()

    @Test
    fun `test 1`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("12", listOf("aaa")), AccuracyLevel.SLICE_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("169", listOf("ccc")), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("179", listOf("ddd")), AccuracyLevel.SLICE_MATCHED)
            )
        )

        // when
        val analysed = tested.analyseOutput(input)

        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("169", analysed[0].number)
    }

    @Test
    fun `test 2`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("12", listOf("aaa")), AccuracyLevel.SLICE_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("1", listOf("eee")), AccuracyLevel.NUMBER_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("169", listOf("ccc")), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("179", listOf("ddd")), AccuracyLevel.SLICE_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("1", listOf("eee")), AccuracyLevel.NUMBER_MATCHED)
            )
        )

        // when
        val analysed = tested.analyseOutput(input)

        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("1", analysed[0].number)
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("12", listOf("aaa")), AccuracyLevel.SLICE_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("1", listOf("eee")), AccuracyLevel.NUMBER_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("169", listOf("ccc")), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("179", listOf("ddd")), AccuracyLevel.SLICE_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
            ),
            listOf(
                LineWithAccuracy(Line("1", listOf("eee")), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
            )
        )

        // when
        val analysed = tested.analyseOutput(input)

        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("1", analysed[0].number)
    }

    @Test
    fun `test 4`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("1", listOf("eee")), AccuracyLevel.NUMBER_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
            ),
            listOf(
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("16", listOf("bbb")), AccuracyLevel.DESTINATION_MATCHED),
            )
        )

        // when
        val analysed = tested.analyseOutput(input)

        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("1", analysed[0].number)
    }
}