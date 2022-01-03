package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.steps.AccuracyInfo
import com.project.hello.vehicle.prediction.steps.AccuracyLevel
import com.project.hello.vehicle.prediction.steps.LineWithAccuracy
import kotlin.test.Test
import kotlin.test.assertEquals

typealias LWA = LineWithAccuracy
typealias AI = AccuracyInfo
typealias AL = AccuracyLevel

internal class OutputAnalysisImplTest {

    val tested = OutputAnalysisImpl()

    @Test
    fun test_1() {
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
        assertEquals("169", analysed!!.number)
    }

    @Test
    fun test_2() {
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
        assertEquals("1", predicted!!.number)
    }

    @Test
    fun test_3() {
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
        assertEquals("1", predicted!!.number)
    }

    @Test
    fun test_4() {
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
        assertEquals("16", predicted!!.number)
    }

    @Test
    fun test_5() {
        // given
        val input = listOf(
            LWA("", Line("16", "a"), AI(AL.NUMBER_SLICE, 99)),
            LWA("", Line("169", "a"), AI(AL.NUMBER_MATCHED, 100)),
            LWA("", Line("1", "a"), AI(AL.NUMBER_SLICE, 99))
        )

        //when
        val predicted = tested.mostProbableLine(input)

        // then
        assertEquals("169", predicted!!.number)
    }
}