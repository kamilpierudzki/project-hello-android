package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.CountryCharactersProvider
import com.project.hello.vehicle.domain.steps.TimeoutChecker
import org.junit.Assert
import org.junit.Test

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
    fun `test 1`() {
        // given
        val input = listOf("16", "ossobieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(7, linesMatchedBasedOnInput.size)
    }

    @Test
    fun `test 2`() {
        // given
        val input = listOf("16")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(3, linesMatchedBasedOnInput.size)

        Assert.assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_MATCHED,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )

        Assert.assertEquals("169", linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )

        Assert.assertEquals("1", linesMatchedBasedOnInput[2].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[2].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf("6")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(2, linesMatchedBasedOnInput.size)

        Assert.assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )

        Assert.assertEquals("169", linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun `test 4`() {
        // given
        val input = listOf("ego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(4, linesMatchedBasedOnInput.size)

        Assert.assertEquals("12", linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )

        Assert.assertEquals("16", linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )

        Assert.assertEquals("169", linesMatchedBasedOnInput[2].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[2].accuracyInfo.accuracyLevel
        )

        Assert.assertEquals("174", linesMatchedBasedOnInput[3].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[3].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun `test 5`() {
        // given
        val input = listOf("789", "blaskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(0, linesMatchedBasedOnInput.size)
    }

    @Test
    fun `test 6`() {
        // given
        val input = listOf("16", "ossobieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(7, linesMatchedBasedOnInput.size)
    }

    @Test
    fun `test 7`() {
        // given
        val input = listOf("16", "os.sobieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, emptyList(), timeoutChecker)

        // then
        Assert.assertEquals(0, linesMatchedBasedOnInput.size)
    }

    @Test
    fun `test 8`() {
        // given
        val input = emptyList<String>()

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, emptyList(), timeoutChecker)

        // then
        Assert.assertEquals(0, linesMatchedBasedOnInput.size)
    }

    @Test
    fun `test 9`() {
        // given
        val input = listOf("")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(0, linesMatchedBasedOnInput.size)
    }

    @Test
    fun `test 10`() {
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
        Assert.assertEquals(1, linesMatchedBasedOnInput.size)

        Assert.assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun `test 11`() {
        // given
        val input = listOf("16O")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(2, linesMatchedBasedOnInput.size)

        Assert.assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )

        Assert.assertEquals("1", linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
    }

    @Test
    fun `test 13`() {
        // given
        val input = listOf("16ossobieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(5, linesMatchedBasedOnInput.size)
        Assert.assertEquals("12", linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(86, linesMatchedBasedOnInput[0].accuracyInfo.percentage)

        Assert.assertEquals("16", linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(13, linesMatchedBasedOnInput[1].accuracyInfo.percentage)

        Assert.assertEquals("169", linesMatchedBasedOnInput[2].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[2].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(86, linesMatchedBasedOnInput[2].accuracyInfo.percentage)

        Assert.assertEquals("174", linesMatchedBasedOnInput[3].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            linesMatchedBasedOnInput[3].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(86, linesMatchedBasedOnInput[3].accuracyInfo.percentage)

        Assert.assertEquals("1", linesMatchedBasedOnInput[4].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[4].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(6, linesMatchedBasedOnInput[4].accuracyInfo.percentage)
    }

    @Test
    fun `test 14`() {
        // given
        val input = listOf("169")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(3, linesMatchedBasedOnInput.size)
        Assert.assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(66, linesMatchedBasedOnInput[0].accuracyInfo.percentage)

        Assert.assertEquals("169", linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_MATCHED,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(100, linesMatchedBasedOnInput[1].accuracyInfo.percentage)

        Assert.assertEquals("1", linesMatchedBasedOnInput[2].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[2].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(33, linesMatchedBasedOnInput[2].accuracyInfo.percentage)
    }

    @Test
    fun `test 15`() {
        // given
        val input = listOf("16os.soelbieskiego")

        // when
        val linesMatchedBasedOnInput =
            tested.cityLinesMatchedBasedOnInput(input, globalCityLines, timeoutChecker)

        // then
        Assert.assertEquals(2, linesMatchedBasedOnInput.size)
        Assert.assertEquals("16", linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[0].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(11, linesMatchedBasedOnInput[0].accuracyInfo.percentage)

        Assert.assertEquals("1", linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            linesMatchedBasedOnInput[1].accuracyInfo.accuracyLevel
        )
        Assert.assertEquals(5, linesMatchedBasedOnInput[1].accuracyInfo.percentage)
    }
}