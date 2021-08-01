package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.city.plan.domain.model.Line
import com.project.hallo.vehicle.domain.steps.AccuracyLevel
import org.junit.Assert
import org.junit.Test

internal class FindingLinesImplTest {

    val globalCityLines1 = listOf(
        Line("12", "os.sobieskiego"),
        Line("16", "os.sobieskiego"),
        Line("169", "os.sobieskiego"),
        Line("174", "os.sobieskiego"),
        Line("1", "franowo"),
    )

    val globalCityLines2 = listOf(
        Line("12", "Os. Sobieskiego"),
        Line("16", "Os. Sobieskiego"),
        Line("169", "Os. Sobieskiego"),
        Line("174", "Os. Sobieskiego"),
        Line("1", "Franowo"),
    )

    @Test
    fun `test 1`() {
        // given
        val tested = FindingLinesImpl()
        val input = listOf("16", "os.sobieskiego")

        // when
        val foundData = tested.foundLinesData(input, globalCityLines1)

        // then
        Assert.assertEquals(6, foundData.matchedLines.size)
        Assert.assertEquals(2, foundData.textsUsedInMatch.size)
    }

    @Test
    fun `test 2`() {
        // given
        val tested = FindingLinesImpl()
        val input = listOf("16")

        // when
        val foundData = tested.foundLinesData(input, globalCityLines1)

        // then
        Assert.assertEquals(2, foundData.matchedLines.size)
        Assert.assertEquals(1, foundData.textsUsedInMatch.size)
        Assert.assertEquals(globalCityLines1[1].number, foundData.matchedLines[0].line.number)
        Assert.assertEquals(AccuracyLevel.NUMBER_MATCHED, foundData.matchedLines[0].accuracyLevel)
        Assert.assertEquals(globalCityLines1[2].number, foundData.matchedLines[1].line.number)
        Assert.assertEquals(AccuracyLevel.SLICE_MATCHED, foundData.matchedLines[1].accuracyLevel)
    }

    @Test
    fun `test 3`() {
        // given
        val tested = FindingLinesImpl()
        val input = listOf("6")

        // when
        val foundData = tested.foundLinesData(input, globalCityLines1)

        // then
        Assert.assertEquals(2, foundData.matchedLines.size)
        Assert.assertEquals(1, foundData.textsUsedInMatch.size)
        Assert.assertEquals(globalCityLines1[1].number, foundData.matchedLines[0].line.number)
        Assert.assertEquals(AccuracyLevel.SLICE_MATCHED, foundData.matchedLines[0].accuracyLevel)
        Assert.assertEquals(globalCityLines1[2].number, foundData.matchedLines[1].line.number)
        Assert.assertEquals(AccuracyLevel.SLICE_MATCHED, foundData.matchedLines[1].accuracyLevel)
    }

    @Test
    fun `test 4`() {
        // given
        val tested = FindingLinesImpl()
        val input = listOf("ego")

        // when
        val foundData = tested.foundLinesData(input, globalCityLines1)

        // then
        Assert.assertEquals(4, foundData.matchedLines.size)
        Assert.assertEquals(1, foundData.textsUsedInMatch.size)
        Assert.assertEquals(globalCityLines1[0].number, foundData.matchedLines[0].line.number)
        Assert.assertEquals(AccuracyLevel.SLICE_MATCHED, foundData.matchedLines[0].accuracyLevel)

        Assert.assertEquals(globalCityLines1[1].number, foundData.matchedLines[1].line.number)
        Assert.assertEquals(AccuracyLevel.SLICE_MATCHED, foundData.matchedLines[1].accuracyLevel)

        Assert.assertEquals(globalCityLines1[2].number, foundData.matchedLines[2].line.number)
        Assert.assertEquals(AccuracyLevel.SLICE_MATCHED, foundData.matchedLines[2].accuracyLevel)

        Assert.assertEquals(globalCityLines1[3].number, foundData.matchedLines[3].line.number)
        Assert.assertEquals(AccuracyLevel.SLICE_MATCHED, foundData.matchedLines[3].accuracyLevel)
    }

    @Test
    fun `test 5`() {
        // given
        val tested = FindingLinesImpl()
        val input = listOf("789", "blaskiego")

        // when
        val foundData = tested.foundLinesData(input, globalCityLines1)

        // then
        Assert.assertEquals(0, foundData.matchedLines.size)
        Assert.assertEquals(0, foundData.textsUsedInMatch.size)
    }

    @Test
    fun `test 6`() {
        // given
        val tested = FindingLinesImpl()
        val input = listOf("16", "os.sobieskiego")

        // when
        val foundData = tested.foundLinesData(input, globalCityLines2)

        // then
        Assert.assertEquals(6, foundData.matchedLines.size)
        Assert.assertEquals(2, foundData.textsUsedInMatch.size)
    }

    @Test
    fun `test 7`() {
        // given
        val tested = FindingLinesImpl()
        val input = listOf("16", "os.sobieskiego")

        // when
        val foundData = tested.foundLinesData(input, emptyList())

        // then
        Assert.assertEquals(0, foundData.matchedLines.size)
        Assert.assertEquals(0, foundData.textsUsedInMatch.size)
    }

    @Test
    fun `test 8`() {
        // given
        val tested = FindingLinesImpl()
        val input = emptyList<String>()

        // when
        val foundData = tested.foundLinesData(input, emptyList())

        // then
        Assert.assertEquals(0, foundData.matchedLines.size)
        Assert.assertEquals(0, foundData.textsUsedInMatch.size)
    }

    @Test
    fun `test 9`() {
        // given
        val tested = FindingLinesImpl()
        val input = listOf("")

        // when
        val foundData = tested.foundLinesData(input, globalCityLines2)

        // then
        Assert.assertEquals(0, foundData.matchedLines.size)
        Assert.assertEquals(0, foundData.textsUsedInMatch.size)
    }
}