package com.project.halo.vehicle.domain.analysis

import com.project.hallo.city.plan.domain.Line
import com.project.halo.vehicle.prediction.data.analysis.LineWithProbability
import com.project.halo.vehicle.prediction.data.analysis.PredictedLinesAnalysis

private const val LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS = 7_000L
private const val ELEMENT_NOT_FOUND_IN_MEMORY = Long.MAX_VALUE

class PredictedLinesAnalysisImpl : PredictedLinesAnalysis {

    private val linesInMemory = mutableMapOf<Line, AnalysisSpecs>()
    private var lastOutput: List<LineWithProbability>? = null

    override fun analysedLines(
        newLines: List<Line>,
        currentTimeInMillis: Long,
        onDataChanged: (List<LineWithProbability>) -> Unit
    ) {
        removeExpiredLinesFromMemory(currentTimeInMillis)
        updateMemoryIfPossible(currentTimeInMillis, newLines)
        val newOutput = createOutputOfMemory()
        if (lastOutput == null || lastOutput.hashCode() != newOutput.hashCode()) {
            lastOutput = newOutput
            onDataChanged.invoke(newOutput)
        }
    }

    private fun removeExpiredLinesFromMemory(currentTimeInMillis: Long) {
        linesInMemory.entries.removeIf {
            isElementExpired(currentTimeInMillis, it.value)
        }
    }

    private fun isElementExpired(currentTimeInMillis: Long, analysisSpecs: AnalysisSpecs): Boolean =
        (currentTimeInMillis - analysisSpecs.latestTimestampOfOccurrence) >= LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS

    private fun updateMemoryIfPossible(currentTimeInMillis: Long, newLines: List<Line>) {
        for (line in newLines) {
            val elementSpecs = linesInMemory.getOrDefault(line, AnalysisSpecs.EMPTY)

            if (isElementExpired(currentTimeInMillis, elementSpecs)) {
                linesInMemory.remove(line)
            } else {
                val howManyTimesOccurred = elementSpecs.howManyTimesOccurred
                val newElementSpecs = elementSpecs.copy(
                    latestTimestampOfOccurrence = currentTimeInMillis,
                    howManyTimesOccurred = howManyTimesOccurred + 1
                )
                linesInMemory[line] = newElementSpecs
            }
        }
    }

    private fun createOutputOfMemory(): List<LineWithProbability> {
        val numberOfAllOccurrences = linesInMemory.entries
            .map { it.value.howManyTimesOccurred }
            .reduceOrNull { acc, i -> acc + i } ?: 0

        return linesInMemory
            .entries
            .map {
                val probability: Float =
                    it.value.howManyTimesOccurred / (numberOfAllOccurrences * 1.0f)
                LineWithProbability(it.key, probability)
            }
            .sortedByDescending {
                it.probability
            }
            .toList()
    }
}

private data class AnalysisSpecs(
    val latestTimestampOfOccurrence: Long,
    val howManyTimesOccurred: Int
) {

    companion object {
        val EMPTY = AnalysisSpecs(ELEMENT_NOT_FOUND_IN_MEMORY, 0)
    }
}