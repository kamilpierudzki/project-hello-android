package com.project.hello.vehicle.domain.analysis.implementation

import com.project.hello.transit.agency.domain.model.Line
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.analysis.LineWithShare

private const val LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS = 2_000L

class BufferingImpl : Buffering {

    private val memory: MutableList<LineWithBufferedInfo> = mutableListOf()

    override fun bufferedLine(
        currentTimeInMillis: Long,
        newPrediction: Line?
    ): LineWithShare? {
        synchronized(this) {
            removeExpiredElementsFromMemory(currentTimeInMillis)
            if (newPrediction != null) {
                saveNewElementInMemory(newPrediction, currentTimeInMillis)
            }
            return calculatedResultWithProbability()
        }
    }

    private fun calculatedResultWithProbability(): LineWithShare? {
        val groupedLinesWithTotalShare: Map<Line, Int> =
            memory
                .map { it.line }
                .map { line ->
                    val occurrenceInMemory = memory.count { line == it.line }
                    val shareOfEachLineInMemory = (100f / memory.size).toInt()
                    val totalShareOfLine = occurrenceInMemory * shareOfEachLineInMemory
                    line to totalShareOfLine
                }
                .toMap()

        val aggregatedLinesWithTotalShare: List<LineWithShare> = groupedLinesWithTotalShare
            .map {
                LineWithShare(it.key, it.value)
            }

        val sortedAggregatedLinesWithTotalShare: List<LineWithShare> =
            aggregatedLinesWithTotalShare.sortedByDescending { it.share }

        val lineWithTheHighestTotalShare: LineWithShare? =
            sortedAggregatedLinesWithTotalShare.firstOrNull()

        return if (lineWithTheHighestTotalShare != null) {
            val thereAreMoreThanOneLine = sortedAggregatedLinesWithTotalShare.size > 1
            val allPredictedLinesHaveTheSameScore = sortedAggregatedLinesWithTotalShare.all {
                it.share == lineWithTheHighestTotalShare.share
            }
            if (thereAreMoreThanOneLine && allPredictedLinesHaveTheSameScore) {
                null
            } else {
                lineWithTheHighestTotalShare
            }
        } else {
            null
        }
    }

    private fun saveNewElementInMemory(line: Line, insertionTimestampInMillis: Long) {
        val newElement = LineWithBufferedInfo(line, insertionTimestampInMillis)
        memory.add(newElement)
    }

    private fun removeExpiredElementsFromMemory(currentTimeInMillis: Long) {
        val iterator = memory.iterator()
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (isMemoryExpiredNew(currentTimeInMillis, line.insertionTimestampInMillis)) {
                iterator.remove()
            }
        }
    }

    private fun isMemoryExpiredNew(
        currentTimeInMillis: Long,
        lineFromMemoryTimestampInMillis: Long
    ): Boolean =
        (currentTimeInMillis - lineFromMemoryTimestampInMillis) >= LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS
}

private data class LineWithBufferedInfo(
    val line: Line,
    val insertionTimestampInMillis: Long
)