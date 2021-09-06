package com.project.hello.vehicle.prediction.framework.internal.analysis

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.LineWithAccuracy
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

private const val LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS = 1_500L

@ViewModelScoped
internal class PredictedLinesAnalysisImpl @Inject constructor() : PredictedLinesAnalysis {

    private val linesInMemory = ConcurrentHashMap<Line, AnalysisInfo>()

    override fun analysedSortedLines(
        newLines: List<LineWithAccuracy>,
        currentTimeInMillis: Long
    ): List<LineWithProbability> {
        removeExpiredLinesFromMemory(currentTimeInMillis)
        analyseNewLines(currentTimeInMillis, newLines)
        return createSortedOutputOfMemory()
    }

    private fun removeExpiredLinesFromMemory(currentTimeInMillis: Long) {
        val iterator = linesInMemory.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (isElementExpired(currentTimeInMillis, entry.value)) {
                iterator.remove()
            }
        }
    }

    private fun isElementExpired(currentTimeInMillis: Long, analysisInfo: AnalysisInfo): Boolean =
        (currentTimeInMillis - analysisInfo.latestTimestampOfOccurrence) >= LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS

    private fun analyseNewLines(
        currentTimeInMillis: Long,
        newLinesWithAccuracy: List<LineWithAccuracy>
    ) {
        for (lineWithAccuracy in newLinesWithAccuracy) {
            val infoFromMemory: AnalysisInfo? =
                linesInMemory.getOrElse(lineWithAccuracy.line, { null })

            if (infoFromMemory != null) {
                if (isElementExpired(currentTimeInMillis, infoFromMemory)) {
                    linesInMemory.remove(infoFromMemory.line)
                } else {
                    val scoreFromMemory = infoFromMemory.score
                    updateMemory(lineWithAccuracy, currentTimeInMillis, scoreFromMemory)
                }
            } else {
                updateMemory(lineWithAccuracy, currentTimeInMillis)
            }
        }
    }

    private fun updateMemory(
        lineWithAccuracy: LineWithAccuracy,
        currentTimeInMillis: Long,
        scoreFromMemory: Int = 0
    ) {
        val scoreFromIncomingElement = getScoreForAccuracyLevel(lineWithAccuracy.accuracyLevel)
        val newAnalysisInfo = AnalysisInfo(
            line = lineWithAccuracy.line,
            latestTimestampOfOccurrence = currentTimeInMillis,
            score = scoreFromMemory + scoreFromIncomingElement
        )
        linesInMemory[lineWithAccuracy.line] = newAnalysisInfo
    }

    private fun createSortedOutputOfMemory(): List<LineWithProbability> {
        val result = LinkedList<LineWithProbability>()
        val sumOfScore = getSumOfScoreInMemory()
        val iterator = linesInMemory.iterator()
        while (iterator.hasNext()) {
            val entry: MutableMap.MutableEntry<Line, AnalysisInfo> = iterator.next()
            val probability: Float =
                entry.value.score / (sumOfScore * 1.0f)
            result.add(LineWithProbability(entry.key, probability))
        }

        result.sortByDescending { it.probability }
        return result
    }

    private fun getSumOfScoreInMemory(): Int {
        var sumOfScore = 0
        val iterator = linesInMemory.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            sumOfScore += entry.value.score
        }
        return sumOfScore
    }

    private fun getScoreForAccuracyLevel(accuracy: AccuracyLevel): Int =
        when (accuracy) {
            AccuracyLevel.NUMBER_MATCHED -> 4
            AccuracyLevel.DESTINATION_MATCHED -> 3
            AccuracyLevel.NUMBER_SLICE -> 2
            AccuracyLevel.DESTINATION_SLICE -> 1
            AccuracyLevel.NOT_MATCHED -> 0
        }
}

private data class AnalysisInfo(
    val line: Line,
    val latestTimestampOfOccurrence: Long,
    val score: Int
)