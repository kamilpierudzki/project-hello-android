package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.CountryCharactersProvider
import org.junit.Assert
import org.junit.Test

internal class VehiclePredictionImplTest {

    val globalCityLines1 = listOf(
        Line("12", "os.sobieskiego"),
        Line("16", "os.sobieskiego"),
        Line("169", "os.sobieskiego"),
        Line("174", "os.sobieskiego"),
        Line("1", "franowo"),
    )

    val polishCharactersProvider = object : CountryCharactersProvider {
        override fun get() = mapOf("ą" to "a", "ł" to "l")
    }
    val universalTransformation = UniversalTransformationImpl(polishCharactersProvider)

    val textMatching = TextMatchingImpl(universalTransformation)
    val fragmentation = FragmentationImpl()
    val findingLines = MatchingCityLinesImpl(textMatching)
    val reduction = ReductionImpl()
    val outputAnalysis = OutputAnalysisImpl()

    val tested = VehiclePredictionImpl(
        findingLines,
        reduction,
        fragmentation,
        outputAnalysis,
        universalTransformation
    )

    @Test
    fun `test 1`() {
        // given
        val rawInput = "16 os.sobieskiego"

        // when
        val predicted = tested.mostProbableLine(rawInput, globalCityLines1)

        // then
        Assert.assertEquals("16", predicted!!.number)
    }

    @Test
    fun `test 2`() {
        // given
        val rawInput = "16 os.soelbieskiego"

        // when
        val predicted = tested.mostProbableLine(rawInput, globalCityLines1)

        // then
        Assert.assertEquals("16", predicted!!.number)
    }

    @Test
    fun `test 3`() {
        // given
        val rawInput = "777 os.sobieskiego"

        // when
        val predicted = tested.mostProbableLine(rawInput, globalCityLines1)

        // then
        Assert.assertEquals("174", predicted!!.number)
    }

    @Test
    fun `test 4`() {
        // given
        val rawInput = "789 blaskiego"

        // when
        val predicted = tested.mostProbableLine(rawInput, globalCityLines1)

        // then
        Assert.assertEquals("174", predicted!!.number)
    }

    @Test
    fun `test 5`() {
        // given
        val rawInput = ""

        // when
        val predicted = tested.mostProbableLine(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(null, predicted)
    }

    @Test
    fun `test 6`() {
        // given
        val rawInput = ""

        // when
        val predicted = tested.mostProbableLine(rawInput, emptyList())

        // then
        Assert.assertEquals(null, predicted)
    }

    @Test
    fun `test 7`() {
        // given
        val rawInput = "Oo ittextBlocks.map f itlines"

        // when
        val predicted = tested.mostProbableLine(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(null, predicted)
    }

    @Test
    fun `test 8`() {
        // given
        val rawInput = "7777"

        // when
        val predicted = tested.mostProbableLine(rawInput, globalCityLines1)

        // then
        Assert.assertEquals("174", predicted!!.number)
    }

    @Test
    fun `test 9`() {
        // given
        val rawInput = "12"

        // when
        val predicted = tested.mostProbableLine(rawInput, globalCityLines1)

        // then
        Assert.assertEquals("12", predicted!!.number)
    }

    @Test
    fun `test 10`() {
        // given
        val cityLines = listOf(
            Line("12", "Łąkowa"),
            Line("16", "Os. Sobieskiego")
        )
        val rawInput = "Lakowa"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("12", predicted!!.number)
    }

    @Test
    fun `test 11`() {
        // given
        val cityLines = listOf(
            Line("15", "Os. Sobieskiego"),
            Line("16", "Os. Sobieskiego")
        )
        val rawInput = "150s. Scbieskiego"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("15", predicted!!.number)
    }

    @Test
    fun `test 12`() {
        // given
        val cityLines = listOf(
            Line("8", "Górczyn PKM"),
            Line("18", "Ogrody"),
            Line("1", "Franowo"),
            Line("8", "Miłostowo"),
            Line("18", "Piątkowska"),
            Line("1", "Junikowo"),
        )
        val rawInput = "180 Piąterowska"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("18", predicted!!.number)
        Assert.assertEquals("Piątkowska", predicted.destination)
    }

    @Test
    fun `test 13`() {
        // given
        val cityLines = listOf(
            Line("8", "Górczyn PKM"),
            Line("18", "Ogrody"),
            Line("1", "Franowo"),
            Line("8", "Miłostowo"),
            Line("18", "Piątkowska"),
            Line("1", "Junikowo"),
        )
        val rawInput = "18 Ogrody"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("18", predicted!!.number)
        Assert.assertEquals("Ogrody", predicted.destination)
    }

    @Test
    fun `test 16`() {
        // given
        val cityLines = listOf(
            Line("8", "Górczyn PKM"),
            Line("18", "Ogrody"),
            Line("1", "Franowo"),
            Line("8", "Miłostowo"),
            Line("18", "Piątkowska"),
            Line("1", "Junikowo"),
        )
        val rawInput = "18 Piątkowska"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("18", predicted!!.number)
        Assert.assertEquals("Piątkowska", predicted.destination)
    }

    @Test
    fun `test 14`() {
        // given
        val cityLines = listOf(
            Line("8", "Górczyn PKM"),
            Line("18", "Ogrody"),
            Line("1", "Franowo"),
            Line("8", "Miłostowo"),
            Line("18", "Piątkowska"),
            Line("1", "Junikowo"),
        )
        val rawInput = "Ogrody"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("18", predicted!!.number)
        Assert.assertEquals("Ogrody", predicted.destination)
    }

    @Test
    fun `test 15`() {
        // given
        val cityLines = listOf(
            Line("8", "Górczyn PKM"),
            Line("18", "Ogrody"),
            Line("1", "Franowo"),
            Line("8", "Miłostowo"),
            Line("18", "Piątkowska"),
            Line("1", "Junikowo"),
        )
        val rawInput = "stowo"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("8", predicted!!.number)
        Assert.assertEquals("Miłostowo", predicted.destination)
    }

    @Test
    fun `test 17`() {
        // given
        val cityLines = listOf(
            Line("2", "Ogrody"),
            Line("7", "Ogrody"),
            Line("17", "Ogrody"),
            Line("18", "Ogrody"),
        )
        val rawInput = "ogrody 7"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("7", predicted!!.number)
        Assert.assertEquals("Ogrody", predicted.destination)
    }

    @Test
    fun `test 18`() {
        // given
        val cityLines = listOf(
            Line("3", "Włodarska")
        )
        val rawInput = "wloda"

        // when
        val predicted = tested.mostProbableLine(rawInput, cityLines)

        // then
        Assert.assertEquals("3", predicted!!.number)
        Assert.assertEquals("Włodarska", predicted.destination)
    }
}