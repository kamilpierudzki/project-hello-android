package com.project.hello.vehicle.prediction.framework.internal.analysis

import com.project.hello.vehicle.domain.analysis.LineWithAccuracyAndProbability
import com.project.hello.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hello.vehicle.domain.steps.LineWithAccuracy
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

private const val LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS = 2_000L
private const val ELEMENT_NOT_FOUND_IN_MEMORY = Long.MAX_VALUE

@ViewModelScoped
internal class PredictedLinesAnalysisImpl @Inject constructor() : PredictedLinesAnalysis {

    private val linesInMemory = ConcurrentHashMap<LineWithAccuracy, AnalysisSpecs>()

    override fun analysedSortedLines(
        newLines: List<LineWithAccuracy>,
        currentTimeInMillis: Long
    ): List<LineWithAccuracyAndProbability> {
        removeExpiredLinesFromMemory(currentTimeInMillis)
        updateMemoryIfPossible(currentTimeInMillis, newLines)
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

    private fun isElementExpired(currentTimeInMillis: Long, analysisSpecs: AnalysisSpecs): Boolean =
        (currentTimeInMillis - analysisSpecs.latestTimestampOfOccurrence) >= LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS

    private fun updateMemoryIfPossible(currentTimeInMillis: Long, newLines: List<LineWithAccuracy>) {
        for (line in newLines) {
            val elementSpecs = linesInMemory.getOrElse(line, { AnalysisSpecs.EMPTY })

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

    private fun createSortedOutputOfMemory(): List<LineWithAccuracyAndProbability> {
        val result = LinkedList<LineWithAccuracyAndProbability>()
        val numberOfAllOccurrences = getNumberOfAllOccurrences()
        val iterator = linesInMemory.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val probability: Float =
                entry.value.howManyTimesOccurred / (numberOfAllOccurrences * 1.0f)
            result.add(LineWithAccuracyAndProbability(entry.key, probability))
        }

        result.sortByDescending { it.probability }
        return result
    }

    private fun getNumberOfAllOccurrences(): Int {
        val iterator = linesInMemory.iterator()
        var numberOfAllOccurrences = 0
        while (iterator.hasNext()) {
            val entry = iterator.next()
            numberOfAllOccurrences += entry.value.howManyTimesOccurred
        }
        return numberOfAllOccurrences
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