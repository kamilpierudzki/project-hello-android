package com.project.hello.vehicle.domain.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.CountryCharactersProvider
import com.project.hello.vehicle.domain.steps.implementation.*
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
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(1, predicted.size)
        Assert.assertEquals("16", predicted[0].line.number)
    }

    @Test
    fun `test 2`() {
        // given
        val rawInput = "16 os.soelbieskiego"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(1, predicted.size)
        Assert.assertEquals("16", predicted[0].line.number)
    }

    @Test
    fun `test 3`() {
        // given
        val rawInput = "777 os.sobieskiego"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(4, predicted.size)
        Assert.assertEquals(globalCityLines1[0].number, predicted[0].line.number)
        Assert.assertEquals(globalCityLines1[1].number, predicted[1].line.number)
        Assert.assertEquals(globalCityLines1[2].number, predicted[2].line.number)
        Assert.assertEquals(globalCityLines1[3].number, predicted[3].line.number)
    }

    @Test
    fun `test 4`() {
        // given
        val rawInput = "789 blaskiego"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(4, predicted.size)
        Assert.assertEquals(globalCityLines1[0].number, predicted[0].line.number)
        Assert.assertEquals(globalCityLines1[1].number, predicted[1].line.number)
        Assert.assertEquals(globalCityLines1[2].number, predicted[2].line.number)
        Assert.assertEquals(globalCityLines1[3].number, predicted[3].line.number)
    }

    @Test
    fun `test 5`() {
        // given
        val rawInput = ""

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(0, predicted.size)
    }

    @Test
    fun `test 6`() {
        // given
        val rawInput = ""

        // when
        val predicted = tested.processInput(rawInput, emptyList())

        // then
        Assert.assertEquals(0, predicted.size)
    }

    @Test
    fun `test 7`() {
        // given
        val rawInput = "Oo ittextBlocks.map f itlines"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(0, predicted.size)
    }

    @Test
    fun `test 8`() {
        // given
        val rawInput = "7777"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(0, predicted.size)
    }

    @Test
    fun `test 9`() {
        // given
        val rawInput = "12"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(1, predicted.size)
        Assert.assertEquals("12", predicted[0].line.number)
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
        val predicted = tested.processInput(rawInput, cityLines)

        // then
        Assert.assertEquals(1, predicted.size)
        Assert.assertEquals("12", predicted[0].line.number)
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
        val predicted = tested.processInput(rawInput, cityLines)

        // then
        Assert.assertEquals(1, predicted.size)
        Assert.assertEquals("15", predicted[0].line.number)
    }
}