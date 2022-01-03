package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.steps.AccuracyLevel
import com.project.hello.vehicle.prediction.steps.CountryCharactersProvider
import com.project.hello.vehicle.prediction.timeout.TimeoutChecker
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MatchingCityLinesImplTest {

    val polishCharacters = mapOf("ą" to "a", "ę" to "e")

    val countryCharactersProvider: CountryCharactersProvider = object : CountryCharactersProvider {
        override fun get() = polishCharacters
    }

    val universalTransformation = UniversalTransformationImpl(countryCharactersProvider)
    val textMatching = TextMatchingImpl(universalTransformation)
    val timeoutChecker = object : TimeoutChecker {
        override fun isTimeout() = false
    }

    val globalCityLines = listOf(
        Line("12", "Os. Sobieskiego"),
        Line("16", "Os. Sobieskiego"),
        Line("169", "Os. Sobieskiego"),
        Line("174", "Os. Sobieskiego"),
        Line("1", "Franowo"),
    )

    val tested = MatchingCityLinesImpl(textMatching)

    @Test
    fun test_1() {
        // given
        val input = listOf("16", "ossobieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(7, linesMatchedBasedOnInput.size)
    }

    @Test
    fun test_2() {
        // given
        val input = listOf("16")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(3, linesMatchedBasedOnInput.size)

        assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_MATCHED,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )

        assertEquals("169", linesMatchedBasedOnInput[1].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )

        assertEquals("1", linesMatchedBasedOnInput[2].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[2].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun test_3() {
        // given
        val input = listOf("6")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(2, linesMatchedBasedOnInput.size)

        assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )

        assertEquals("169", linesMatchedBasedOnInput[1].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun test_4() {
        // given
        val input = listOf("ego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(4, linesMatchedBasedOnInput.size)

        assertEquals("12", linesMatchedBasedOnInput[0].line.number)
        assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )

        assertEquals("16", linesMatchedBasedOnInput[1].line.number)
        assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )

        assertEquals("169", linesMatchedBasedOnInput[2].line.number)
        assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[2].accuracyInfo.accuracyLevel
        )

        assertEquals("174", linesMatchedBasedOnInput[3].line.number)
        assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[3].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun test_5() {
        // given
        val input = listOf("789", "blaskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(0, linesMatchedBasedOnInput.size)
    }

    @Test
    fun test_6() {
        // given
        val input = listOf("16", "ossobieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(7, linesMatchedBasedOnInput.size)
    }

    @Test
    fun test_7() {
        // given
        val input = listOf("16", "os.sobieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, emptyList(), timeoutChecker)

        // then
        assertEquals(0, linesMatchedBasedOnInput.size)
    }

    @Test
    fun test_8() {
        // given
        val input = emptyList<String>()

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, emptyList(), timeoutChecker)

        // then
        assertEquals(0, linesMatchedBasedOnInput.size)
    }

    @Test
    fun test_9() {
        // given
        val input = listOf("")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(0, linesMatchedBasedOnInput.size)
    }

    @Test
    fun test_10() {
        // given
        val cityLines = listOf(
            Line("16", "Os.Sobieskiego"),
            Line("2", "Franowo"),
        )

        val input = listOf("160s.Scbieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, cityLines, timeoutChecker)

        // then
        assertEquals(1, linesMatchedBasedOnInput.size)

        assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun test_11() {
        // given
        val input = listOf("16O")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(2, linesMatchedBasedOnInput.size)

        assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )

        assertEquals("1", linesMatchedBasedOnInput[1].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun test_13() {
        // given
        val input = listOf("16ossobieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(5, linesMatchedBasedOnInput.size)
        assertEquals("12", linesMatchedBasedOnInput[0].line.number)
        assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )
        assertEquals(86, linesMatchedBasedOnInput[0].accuracyInfo.percentage)

        assertEquals("16", linesMatchedBasedOnInput[1].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
        assertEquals(13, linesMatchedBasedOnInput[1].accuracyInfo.percentage)

        assertEquals("169", linesMatchedBasedOnInput[2].line.number)
        assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[2].accuracyInfo.accuracyLevel
        )
        assertEquals(86, linesMatchedBasedOnInput[2].accuracyInfo.percentage)

        assertEquals("174", linesMatchedBasedOnInput[3].line.number)
        assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[3].accuracyInfo.accuracyLevel
        )
        assertEquals(86, linesMatchedBasedOnInput[3].accuracyInfo.percentage)

        assertEquals("1", linesMatchedBasedOnInput[4].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[4].accuracyInfo.accuracyLevel
        )
        assertEquals(6, linesMatchedBasedOnInput[4].accuracyInfo.percentage)
    }

    @Test
    fun test_14() {
        // given
        val input = listOf("169")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(3, linesMatchedBasedOnInput.size)
        assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )
        assertEquals(66, linesMatchedBasedOnInput[0].accuracyInfo.percentage)

        assertEquals("169", linesMatchedBasedOnInput[1].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_MATCHED,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
        assertEquals(100, linesMatchedBasedOnInput[1].accuracyInfo.percentage)

        assertEquals("1", linesMatchedBasedOnInput[2].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[2].accuracyInfo.accuracyLevel
        )
        assertEquals(33, linesMatchedBasedOnInput[2].accuracyInfo.percentage)
    }

    @Test
    fun test_15() {
        // given
        val input = listOf("16os.soelbieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        assertEquals(2, linesMatchedBasedOnInput.size)
        assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )
        assertEquals(11, linesMatchedBasedOnInput[0].accuracyInfo.percentage)

        assertEquals("1", linesMatchedBasedOnInput[1].line.number)
        assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
        assertEquals(5, linesMatchedBasedOnInput[1].accuracyInfo.percentage)
    }
}