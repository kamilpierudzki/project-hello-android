package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.vehicle.domain.steps.AccuracyInfo
import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.LineWithAccuracy
import com.project.hello.vehicle.domain.steps.OutputAnalysis

class OutputAnalysisImpl : OutputAnalysis {

    override fun mostProbableLine(linesToAnalyse: List<LineWithAccuracy>): Line? {
        val groupedReducedLines: Map<AccuracyLevel, List<LineWithAccuracyAndScore>> = linesToAnalyse
            .groupBy { it.accuracyInfo.accuracyLevel }
            .map {
                val accuracyLevel = it.key
                val linesWithAccuracyAndScore = it.value
                    .map { lineWithAccuracy ->
                        LineWithAccuracyAndScore(
                            lineWithAccuracy,
                            getScoreForAccuracyInfo(lineWithAccuracy.accuracyInfo)
                        )
                    }

                val highestScoredLine = getLineWithHighestLocalScore(linesWithAccuracyAndScore)
                val targetScore = highestScoredLine.score

                val reducedLines = linesWithAccuracyAndScore
                    .asSequence()
                    .filter { lineWithAccuracyAndScore ->
                        val currentScore = lineWithAccuracyAndScore.score
                        currentScore >= targetScore
                    }
                    .toList()

                accuracyLevel to reducedLines
            }
            .toMap()

        val flattenLinesSortedByScore: List<LineWithScore> = groupedReducedLines
            .entries
            .flatMap { entry -> entry.value }
            .sortedByDescending { lineWithAccuracyAndScore -> lineWithAccuracyAndScore.score }
            .map { lineWithAccuracyAndScore ->
                val line = lineWithAccuracyAndScore.lineWithAccuracy.line
                val score = lineWithAccuracyAndScore.score
                LineWithScore(line, score)
            }

        val aggregatedLinesWithTotalSum: List<LineWithScore> = flattenLinesSortedByScore
            .groupBy { it.line }
            .map {
                it.key to it.value.reduce { acc, element ->
                    LineWithScore(it.key, acc.score + element.score)
                }
            }
            .toMap()
            .entries
            .map { it.value }

        val sortedAggregatedLinesWithTotalSum: List<LineWithScore> =
            aggregatedLinesWithTotalSum.sortedByDescending { it.score }

        val lineWithTheHighestTotalSum: LineWithScore? =
            sortedAggregatedLinesWithTotalSum.firstOrNull()

        return if (lineWithTheHighestTotalSum != null) {
            val thereAreMoreThanOneLine = sortedAggregatedLinesWithTotalSum.size > 1
            val allPredictedLinesHaveTheSameScore = sortedAggregatedLinesWithTotalSum.all {
                it.score == lineWithTheHighestTotalSum.score
            }
            if (thereAreMoreThanOneLine && allPredictedLinesHaveTheSameScore) {
                null
            } else {
                lineWithTheHighestTotalSum.line
            }
        } else {
            null
        }
    }

    private fun getLineWithHighestLocalScore(input: List<LineWithAccuracyAndScore>): LineWithAccuracyAndScore {
        var highestScoredLine: LineWithAccuracyAndScore? = null

        val isCurrentScoreHighest: (currentScore: Int) -> Boolean = { currentScore ->
            val score = highestScoredLine?.score ?: -1
            currentScore > score
        }

        for (lineWithAccuracyAndScore in input) {
            val currentScore = lineWithAccuracyAndScore.score
            if (isCurrentScoreHighest(currentScore)) {
                highestScoredLine = lineWithAccuracyAndScore
            }
        }

        return highestScoredLine!!
    }

    private fun getScoreForAccuracyInfo(accuracyInfo: AccuracyInfo): Int {
        return when (accuracyInfo.accuracyLevel) {
            AccuracyLevel.NUMBER_MATCHED -> 400
            AccuracyLevel.DESTINATION_MATCHED -> 300
            AccuracyLevel.NUMBER_SLICE -> 200 - (100 - accuracyInfo.percentage)
            AccuracyLevel.DESTINATION_SLICE -> 100 - (100 - accuracyInfo.percentage)
            AccuracyLevel.NOT_MATCHED -> 0
        }
    }
}

private data class LineWithScore(val line: Line, val score: Int)

private data class LineWithAccuracyAndScore(
    val lineWithAccuracy: LineWithAccuracy,
    val score: Int
)