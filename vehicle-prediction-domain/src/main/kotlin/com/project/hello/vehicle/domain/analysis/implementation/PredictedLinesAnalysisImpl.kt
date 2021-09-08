package com.project.hello.vehicle.domain.analysis.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.analysis.PredictedLinesAnalysis

private const val LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS = 1_500L

class PredictedLinesAnalysisImpl : PredictedLinesAnalysis {

    private var lineInMemory: LineWithBufferedInfo? = null
    private var totalCountOfUpdates: Int = 0

    override fun bufferedLine(
        currentTimeInMillis: Long,
        newPrediction: Line?
    ): LineWithProbability? {
        clearMemoryIfExpiredSynchronized(currentTimeInMillis)
        analyseNewPredictionSynchronized(currentTimeInMillis, newPrediction)
        return getBufferedLineWithProbabilitySynchronized()
    }

    private fun clearMemoryIfExpiredSynchronized(currentTimeInMillis: Long) {
        synchronized(this) {
            if (lineInMemory != null && isMemoryExpired(currentTimeInMillis, lineInMemory!!)) {
                lineInMemory = null
                totalCountOfUpdates = 0
            }
        }
    }

    private fun isMemoryExpired(currentTimeInMillis: Long, memory: LineWithBufferedInfo): Boolean =
        (currentTimeInMillis - memory.latestTimestampOfOccurrence) >= LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS

    private fun analyseNewPredictionSynchronized(currentTimeInMillis: Long, newPrediction: Line?) {
        synchronized(this) {
            if (lineInMemory != null && lineInMemory!!.line == newPrediction) {
                incrementCounterInMemory()
            } else if (lineInMemory == null && newPrediction != null) {
                insertIntoMemory(newPrediction, currentTimeInMillis)
            }
            totalCountOfUpdates += 1
        }
    }

    private fun incrementCounterInMemory() {
        lineInMemory = lineInMemory?.run {
            copy(howManyTimesOccurred = howManyTimesOccurred + 1)
        }
    }

    private fun insertIntoMemory(line: Line, currentTimeInMillis: Long) {
        lineInMemory = LineWithBufferedInfo(
            line = line,
            latestTimestampOfOccurrence = currentTimeInMillis,
            howManyTimesOccurred = 1
        )
    }

    private fun getBufferedLineWithProbabilitySynchronized(): LineWithProbability? {
        synchronized(this) {
            return lineInMemory?.let {
                val probability: Float = it.howManyTimesOccurred / (totalCountOfUpdates * 1.0f)
                LineWithProbability(it.line, probability)
            }
        }
    }
}

private data class LineWithBufferedInfo(
    val line: Line,
    val latestTimestampOfOccurrence: Long,
    val howManyTimesOccurred: Int
)