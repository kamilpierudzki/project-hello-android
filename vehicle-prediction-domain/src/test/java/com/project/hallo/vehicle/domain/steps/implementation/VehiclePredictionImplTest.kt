package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.city.plan.domain.Line
import org.junit.Assert
import org.junit.Test

internal class VehiclePredictionImplTest {

    val globalCityLines1 = listOf(
        Line("12", listOf("os.sobieskiego")),
        Line("16", listOf("os.sobieskiego")),
        Line("169", listOf("os.sobieskiego")),
        Line("174", listOf("os.sobieskiego")),
        Line("1", listOf("franowo")),
    )

    val fragmentation = FragmentationImpl()
    val findingLines = FindingLinesImpl()
    val reduction = ReductionExperimentalImpl()
    val outputAnalysis = OutputAnalysisImpl()

    val tested = VehiclePredictionImpl(findingLines, reduction, fragmentation, outputAnalysis)

    @Test
    fun `test 1`() {
        // given
        val rawInput = "16 os.sobieskiego"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(1, predicted.size)
        Assert.assertEquals(globalCityLines1[1].number, predicted[0].number)
    }

    @Test
    fun `test 2`() {
        // given
        val rawInput = "16 os.soelbieskiego"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(1, predicted.size)
        Assert.assertEquals(globalCityLines1[1].number, predicted[0].number)
    }

    @Test
    fun `test 3`() {
        // given
        val rawInput = "777 os.sobieskiego"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(4, predicted.size)
        Assert.assertEquals(globalCityLines1[0].number, predicted[0].number)
        Assert.assertEquals(globalCityLines1[1].number, predicted[1].number)
        Assert.assertEquals(globalCityLines1[2].number, predicted[2].number)
        Assert.assertEquals(globalCityLines1[3].number, predicted[3].number)
    }

    @Test
    fun `test 4`() {
        // given
        val rawInput = "789 blaskiego"

        // when
        val predicted = tested.processInput(rawInput, globalCityLines1)

        // then
        Assert.assertEquals(4, predicted.size)
        Assert.assertEquals(globalCityLines1[0].number, predicted[0].number)
        Assert.assertEquals(globalCityLines1[1].number, predicted[1].number)
        Assert.assertEquals(globalCityLines1[2].number, predicted[2].number)
        Assert.assertEquals(globalCityLines1[3].number, predicted[3].number)
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
        Assert.assertEquals("12", predicted[0].number)
    }
}