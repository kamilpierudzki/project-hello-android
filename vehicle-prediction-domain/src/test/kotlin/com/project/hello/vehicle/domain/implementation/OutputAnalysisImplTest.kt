package com.project.hello.vehicle.domain.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.LineWithAccuracy
import com.project.hello.vehicle.domain.steps.implementation.OutputAnalysisImpl
import org.junit.Assert
import org.junit.Test

internal class OutputAnalysisImplTest {

    val tested = OutputAnalysisImpl()

    @Test
    fun `test 1`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("12", "aaa"), AccuracyLevel.NUMBER_SLICE),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("169", "ccc"), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("179", "ddd"), AccuracyLevel.DESTINATION_SLICE)
            )
        )

        // when
        val analysed = tested.analysedOutputMatrix(input)

        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("169", analysed[0].line.number)
    }

    @Test
    fun `test 2`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("12", "aaa"), AccuracyLevel.NUMBER_SLICE),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("1", "eee"), AccuracyLevel.NUMBER_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("169", "ccc"), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("179", "ddd"), AccuracyLevel.NUMBER_SLICE)
            ),
            listOf(
                LineWithAccuracy(Line("1", "eee"), AccuracyLevel.NUMBER_MATCHED)
            )
        )

        // when
        val analysed = tested.analysedOutputMatrix(input)

        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("1", analysed[0].line.number)
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("12", "aaa"), AccuracyLevel.NUMBER_SLICE),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("1", "eee"), AccuracyLevel.NUMBER_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("169", "ccc"), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("179", "ddd"), AccuracyLevel.NUMBER_SLICE),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
            ),
            listOf(
                LineWithAccuracy(Line("1", "eee"), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
            )
        )

        // when
        val analysed = tested.analysedOutputMatrix(input)

        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("1", analysed[0].line.number)
    }

    @Test
    fun `test 4`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("1", "eee"), AccuracyLevel.NUMBER_MATCHED)
            ),
            listOf(
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
            ),
            listOf(
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
                LineWithAccuracy(Line("16", "bbb"), AccuracyLevel.DESTINATION_MATCHED),
            )
        )

        // when
        val analysed = tested.analysedOutputMatrix(input)

        // then
        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("1", analysed[0].line.number)
    }

    @Test
    fun `test 5`() {
        // given
        val input = listOf(
            listOf(
                LineWithAccuracy(Line("16", "a"), AccuracyLevel.NUMBER_SLICE),
                LineWithAccuracy(Line("169", "a"), AccuracyLevel.NUMBER_MATCHED),
                LineWithAccuracy(Line("1", "a"), AccuracyLevel.NUMBER_SLICE)
            )
        )

        //when
        val analysed = tested.analysedOutputMatrix(input)

        // then
        Assert.assertEquals(1, analysed.size)
        Assert.assertEquals("169", analysed[0].line.number)
    }
}