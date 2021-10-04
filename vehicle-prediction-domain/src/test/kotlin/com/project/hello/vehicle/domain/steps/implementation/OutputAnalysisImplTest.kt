package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.vehicle.domain.steps.AccuracyInfo
import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.LineWithAccuracy
import org.junit.Assert
import org.junit.Test

typealias LWA = LineWithAccuracy
typealias AI = AccuracyInfo
typealias AL = AccuracyLevel

internal class OutputAnalysisImplTest {

    val tested = OutputAnalysisImpl()

    @Test
    fun `test 1`() {
        // given
        val input = listOf(
            LWA("", Line("12", "aaa"), AI(AL.NUMBER_SLICE, 7)),
            LWA("", Line("16", "bbb"), AI(AL.DESTINATION_MATCHED, 100)),
            LWA("", Line("169", "ccc"), AI(AL.NUMBER_MATCHED, 66)),
            LWA("", Line("179", "ddd"), AI(AL.DESTINATION_SLICE, 33))
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
            LWA("", Line("12", "aaa"), AI(AL.NUMBER_SLICE, 50)),
            LWA("", Line("16", "bbb"), AI(AL.DESTINATION_MATCHED, 100)),
            LWA("", Line("1", "eee"), AI(AL.NUMBER_MATCHED, 30)),
            LWA("", Line("169", "ccc"), AI(AL.NUMBER_MATCHED, 20)),
            LWA("", Line("179", "ddd"), AI(AL.NUMBER_SLICE, 10)),
            LWA("", Line("1", "eee"), AI(AL.NUMBER_MATCHED, 100))
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
            LWA("", Line("12", "aaa"), AI(AL.NUMBER_SLICE, 10)),
            LWA("", Line("179", "ddd"), AI(AL.NUMBER_SLICE, 20)),
            LWA("", Line("16", "bbb"), AI(AL.DESTINATION_MATCHED, 100)),
            LWA("", Line("1", "eee"), AI(AL.NUMBER_MATCHED, 100)),
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
            LWA("", Line("16", "bbb"), AI(AL.DESTINATION_MATCHED, 100)),
            LWA("", Line("16", "bbb"), AI(AL.DESTINATION_MATCHED, 100)),
            LWA("", Line("1", "eee"), AI(AL.NUMBER_MATCHED, 100)),
            LWA("", Line("16", "bbb"), AI(AL.DESTINATION_MATCHED, 100)),
            LWA("", Line("1", "bbb"), AI(AL.NUMBER_MATCHED, 100)),
            LWA("", Line("16", "bbb"), AI(AL.DESTINATION_MATCHED, 100)),
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
            LWA("", Line("16", "a"), AI(AL.NUMBER_SLICE, 99)),
            LWA("", Line("169", "a"), AI(AL.NUMBER_MATCHED, 100)),
            LWA("", Line("1", "a"), AI(AL.NUMBER_SLICE, 99))
        )

        //when
        val predicted = tested.mostProbableLine(input)

        // then
        Assert.assertEquals("169", predicted!!.number)
    }
}