package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.*
import com.project.hello.vehicle.domain.steps.implementation.*
import org.junit.Assert
import org.junit.Test

internal class OutputAnalysisImplTest {

    val tested = OutputAnalysisImpl()

    @Test
    fun `test 1`() {
        // given
        val input = listOf(
            LineWithAccuracy(
                "",
                Line("12", "aaa"),
                AccuracyInfo(AccuracyLevel.NUMBER_SLICE, 7)
            ),
            LineWithAccuracy(
                "",
                Line("16", "bbb"),
                AccuracyInfo(AccuracyLevel.DESTINATION_MATCHED, 100)
            ),
            LineWithAccuracy(
                "",
                Line("169", "ccc"),
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, 66)
            ),
            LineWithAccuracy(
                "",
                Line("179", "ddd"),
                AccuracyInfo(AccuracyLevel.DESTINATION_SLICE, 33)
            )
        )

        // when
        val analysed = tested.mostProbableLine(input)

        // then
        Assert.assertEquals("169", analysed!!.number)
    }

    @Test
    fun `test 2`() {
        // given
        val input = listOf(
            LineWithAccuracy(
                "", Line("12", "aaa"),
                AccuracyInfo(AccuracyLevel.NUMBER_SLICE, 50)
            ),
            LineWithAccuracy(
                "", Line("16", "bbb"),
                AccuracyInfo(AccuracyLevel.DESTINATION_MATCHED, 100)
            ),
            LineWithAccuracy(
                "", Line("1", "eee"),
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, 30)
            ),
            LineWithAccuracy(
                "", Line("169", "ccc"),
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, 20)
            ),
            LineWithAccuracy(
                "", Line("179", "ddd"),
                AccuracyInfo(AccuracyLevel.NUMBER_SLICE, 10)
            ),
            LineWithAccuracy(
                "", Line("1", "eee"),
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, 100)
            )
        )

        // when
        val predicted = tested.mostProbableLine(input)

        // then
        Assert.assertEquals("1", predicted!!.number)
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf(
            LineWithAccuracy(
                "", Line("12", "aaa"),
                AccuracyInfo(AccuracyLevel.NUMBER_SLICE, 10)
            ),
            LineWithAccuracy(
                "", Line("179", "ddd"),
                AccuracyInfo(AccuracyLevel.NUMBER_SLICE, 20)
            ),
            LineWithAccuracy(
                "", Line("16", "bbb"),
                AccuracyInfo(AccuracyLevel.DESTINATION_MATCHED, 100)
            ),
            LineWithAccuracy(
                "", Line("1", "eee"),
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, 100)
            ),
        )

        // when
        val predicted = tested.mostProbableLine(input)

        // then
        Assert.assertEquals("1", predicted!!.number)
    }

    @Test
    fun `test 4`() {
        // given
        val input = listOf(
            LineWithAccuracy(
                "", Line("16", "bbb"),
                AccuracyInfo(AccuracyLevel.DESTINATION_MATCHED, 100)
            ),
            LineWithAccuracy(
                "", Line("16", "bbb"),
                AccuracyInfo(AccuracyLevel.DESTINATION_MATCHED, 100)
            ),
            LineWithAccuracy(
                "", Line("1", "eee"),
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, 100)
            ),
            LineWithAccuracy(
                "", Line("16", "bbb"),
                AccuracyInfo(AccuracyLevel.DESTINATION_MATCHED, 100)
            ),
            LineWithAccuracy(
                "", Line("1", "bbb"),
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, 100)
            ),
            LineWithAccuracy(
                "", Line("16", "bbb"),
                AccuracyInfo(AccuracyLevel.DESTINATION_MATCHED, 100)
            ),
        )

        // when
        val predicted = tested.mostProbableLine(input)

        // then
        Assert.assertEquals("16", predicted!!.number)
    }

    @Test
    fun `test 5`() {
        // given
        val input = listOf(
            LineWithAccuracy(
                "", Line("16", "a"),
                AccuracyInfo(AccuracyLevel.NUMBER_SLICE, 99)
            ),
            LineWithAccuracy(
                "", Line("169", "a"),
                AccuracyInfo(AccuracyLevel.NUMBER_MATCHED, 100)
            ),
            LineWithAccuracy(
                "", Line("1", "a"),
                AccuracyInfo(AccuracyLevel.NUMBER_SLICE, 99)
            )
        )

        //when
        val predicted = tested.mostProbableLine(input)

        // then
        Assert.assertEquals("169", predicted!!.number)
    }
}