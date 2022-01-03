package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.steps.CountryCharactersProvider
import com.project.hello.vehicle.prediction.timeout.TimeoutChecker
import kotlin.test.Test
import kotlin.test.assertEquals

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
    val timeoutChecker = object : TimeoutChecker {
        override fun isTimeout() = false
    }

    val tested = VehiclePredictionImpl(
        findingLines,
        reduction,
        fragmentation,
        outputAnalysis,
        universalTransformation
    )

    @Test
    fun test_1() {
        // given
        val rawInput = "16 os.sobieskiego"

        // when
        val predicted = tested.predictLine(rawInput, globalCityLines1, timeoutChecker)

        // then
        assertEquals("16", predicted!!.number)
    }

    @Test
    fun test_2() {
        // given
        val rawInput = "16 os.soelbieskiego"

        // when
        val predicted = tested.predictLine(rawInput, globalCityLines1, timeoutChecker)

        // then
        assertEquals("16", predicted!!.number)
    }

    @Test
    fun test_3() {
        // given
        val rawInput = "777 os.sobieskiego"

        // when
        val predicted = tested.predictLine(rawInput, globalCityLines1, timeoutChecker)

        // then
        assertEquals("174", predicted!!.number)
    }

    @Test
    fun test_4() {
        // given
        val rawInput = "789 blaskiego"

        // when
        val predicted = tested.predictLine(rawInput, globalCityLines1, timeoutChecker)

        // then
        assertEquals("174", predicted!!.number)
    }

    @Test
    fun test_5() {
        // given
        val rawInput = ""

        // when
        val predicted = tested.predictLine(rawInput, globalCityLines1, timeoutChecker)

        // then
        assertEquals(null, predicted)
    }

    @Test
    fun test_6() {
        // given
        val rawInput = ""

        // when
        val predicted = tested.predictLine(rawInput, emptyList(), timeoutChecker)

        // then
        assertEquals(null, predicted)
    }

    @Test
    fun test_7() {
        // given
        val rawInput = "Oo ittextBlocks.map f itlines"

        // when
        val predicted = tested.predictLine(rawInput, globalCityLines1, timeoutChecker)

        // then
        assertEquals(null, predicted)
    }

    @Test
    fun test_8() {
        // given
        val rawInput = "7777"

        // when
        val predicted = tested.predictLine(rawInput, globalCityLines1, timeoutChecker)

        // then
        assertEquals("174", predicted!!.number)
    }

    @Test
    fun test_9() {
        // given
        val rawInput = "12"

        // when
        val predicted = tested.predictLine(rawInput, globalCityLines1, timeoutChecker)

        // then
        assertEquals("12", predicted!!.number)
    }

    @Test
    fun test_10() {
        // given
        val cityLines = listOf(
            Line("12", "Łąkowa"),
            Line("16", "Os. Sobieskiego")
        )
        val rawInput = "Lakowa"

        // when
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("12", predicted!!.number)
    }

    @Test
    fun test_11() {
        // given
        val cityLines = listOf(
            Line("15", "Os. Sobieskiego"),
            Line("16", "Os. Sobieskiego")
        )
        val rawInput = "150s. Scbieskiego"

        // when
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("15", predicted!!.number)
    }

    @Test
    fun test_12() {
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
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("18", predicted!!.number)
        assertEquals("Piątkowska", predicted.destination)
    }

    @Test
    fun test_13() {
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
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("18", predicted!!.number)
        assertEquals("Ogrody", predicted.destination)
    }

    @Test
    fun test_16() {
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
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("18", predicted!!.number)
        assertEquals("Piątkowska", predicted.destination)
    }

    @Test
    fun test_14() {
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
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("18", predicted!!.number)
        assertEquals("Ogrody", predicted.destination)
    }

    @Test
    fun test_15() {
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
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("8", predicted!!.number)
        assertEquals("Miłostowo", predicted.destination)
    }

    @Test
    fun test_17() {
        // given
        val cityLines = listOf(
            Line("2", "Ogrody"),
            Line("7", "Ogrody"),
            Line("17", "Ogrody"),
            Line("18", "Ogrody"),
        )
        val rawInput = "ogrody 7"

        // when
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("7", predicted!!.number)
        assertEquals("Ogrody", predicted.destination)
    }

    @Test
    fun test_18() {
        // given
        val cityLines = listOf(
            Line("3", "Włodarska")
        )
        val rawInput = "wloda"

        // when
        val predicted = tested.predictLine(rawInput, cityLines, timeoutChecker)

        // then
        assertEquals("3", predicted!!.number)
        assertEquals("Włodarska", predicted.destination)
    }

    @Test
    fun test_19() {
        // given
        val lines = listOf(
            Line("12", "os.sobieskiego"),
            Line("16", "os.sobieskiego"),
            Line("169", "os.sobieskiego"),
            Line("174", "os.sobieskiego"),
        )
        val rawInput = "sobie"

        // when
        val predicted = tested.predictLine(rawInput, lines, timeoutChecker)

        // then
        assertEquals(null, predicted)
    }
}