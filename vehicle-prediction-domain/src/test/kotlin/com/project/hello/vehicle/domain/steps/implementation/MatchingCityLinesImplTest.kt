package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.CountryCharactersProvider
import org.junit.Assert
import org.junit.Test

internal class MatchingCityLinesImplTest {

    val polishCharacters = mapOf("ą" to "a", "ę" to "e")

    val countryCharactersProvider: CountryCharactersProvider = object : CountryCharactersProvider {
        override fun get() = polishCharacters
    }

    val universalTransformation = UniversalTransformationImpl(countryCharactersProvider)
    val textMatching = TextMatchingImpl(universalTransformation)

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
        val input = listOf("16", "os.sobieskiego")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(7, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(2, matchingInfo.textsFromInputUsedToMatch.size)
    }

    @Test
    fun `test 2`() {
        // given
        val input = listOf("16")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(3, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(1, matchingInfo.textsFromInputUsedToMatch.size)

        Assert.assertEquals("16", matchingInfo.linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_MATCHED,
            matchingInfo.linesMatchedBasedOnInput[0].accuracyLevel
        )

        Assert.assertEquals("169", matchingInfo.linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            matchingInfo.linesMatchedBasedOnInput[1].accuracyLevel
        )

        Assert.assertEquals("1", matchingInfo.linesMatchedBasedOnInput[2].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            matchingInfo.linesMatchedBasedOnInput[2].accuracyLevel
        )
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf("6")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(2, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(1, matchingInfo.textsFromInputUsedToMatch.size)

        Assert.assertEquals("16", matchingInfo.linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            matchingInfo.linesMatchedBasedOnInput[0].accuracyLevel
        )

        Assert.assertEquals("169", matchingInfo.linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            matchingInfo.linesMatchedBasedOnInput[1].accuracyLevel
        )
    }

    @Test
    fun `test 4`() {
        // given
        val input = listOf("ego")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(4, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(1, matchingInfo.textsFromInputUsedToMatch.size)

        Assert.assertEquals("12", matchingInfo.linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            matchingInfo.linesMatchedBasedOnInput[0].accuracyLevel
        )

        Assert.assertEquals("16", matchingInfo.linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            matchingInfo.linesMatchedBasedOnInput[1].accuracyLevel
        )

        Assert.assertEquals("169", matchingInfo.linesMatchedBasedOnInput[2].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            matchingInfo.linesMatchedBasedOnInput[2].accuracyLevel
        )

        Assert.assertEquals("174", matchingInfo.linesMatchedBasedOnInput[3].line.number)
        Assert.assertEquals(
            AccuracyLevel.DESTINATION_SLICE,
            matchingInfo.linesMatchedBasedOnInput[3].accuracyLevel
        )
    }

    @Test
    fun `test 5`() {
        // given
        val input = listOf("789", "blaskiego")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(0, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(0, matchingInfo.textsFromInputUsedToMatch.size)
    }

    @Test
    fun `test 6`() {
        // given
        val input = listOf("16", "os.sobieskiego")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(7, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(2, matchingInfo.textsFromInputUsedToMatch.size)
    }

    @Test
    fun `test 7`() {
        // given
        val input = listOf("16", "os.sobieskiego")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, emptyList())

        // then
        Assert.assertEquals(0, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(0, matchingInfo.textsFromInputUsedToMatch.size)
    }

    @Test
    fun `test 8`() {
        // given
        val input = emptyList<String>()

        // when
        val matchingInfo = tested.matchingLinesInfo(input, emptyList())

        // then
        Assert.assertEquals(0, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(0, matchingInfo.textsFromInputUsedToMatch.size)
    }

    @Test
    fun `test 9`() {
        // given
        val input = listOf("")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(0, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(0, matchingInfo.textsFromInputUsedToMatch.size)
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
        val matchingInfo = tested.matchingLinesInfo(input, cityLines)

        // then
        Assert.assertEquals(1, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(1, matchingInfo.textsFromInputUsedToMatch.size)

        Assert.assertEquals("16", matchingInfo.linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            matchingInfo.linesMatchedBasedOnInput[0].accuracyLevel
        )

    }

    @Test
    fun `test 11`() {
        // given
        val input = listOf("16O")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(2, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(1, matchingInfo.textsFromInputUsedToMatch.size)

        Assert.assertEquals("16", matchingInfo.linesMatchedBasedOnInput[0].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            matchingInfo.linesMatchedBasedOnInput[0].accuracyLevel
        )

        Assert.assertEquals("1", matchingInfo.linesMatchedBasedOnInput[1].line.number)
        Assert.assertEquals(
            AccuracyLevel.NUMBER_SLICE,
            matchingInfo.linesMatchedBasedOnInput[1].accuracyLevel
        )
    }

    @Test
    fun `test 13`() {
        // given
        val input = listOf("16os.sobieskiego")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(5, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(1, matchingInfo.textsFromInputUsedToMatch.size)
    }

    @Test
    fun `test 14`() {
        // given
        val input = listOf("169")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(3, matchingInfo.linesMatchedBasedOnInput.size)
        Assert.assertEquals(1, matchingInfo.textsFromInputUsedToMatch.size)
    }

    @Test
    fun `test 15`() {
        // given
        val input = listOf("16os.soelbieskiego")

        // when
        val matchingInfo = tested.matchingLinesInfo(input, globalCityLines)

        // then
        Assert.assertEquals(2, matchingInfo.linesMatchedBasedOnInput.size)
    }
}