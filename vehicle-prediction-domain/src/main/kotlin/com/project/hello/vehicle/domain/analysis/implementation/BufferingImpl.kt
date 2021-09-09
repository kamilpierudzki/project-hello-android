package com.project.hello.vehicle.domain.analysis.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.domain.analysis.Buffering

private const val LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS = 2_000L

class BufferingImpl : Buffering {

    private val memory: MutableList<LineWithBufferedInfo> = mutableListOf()

    override fun bufferedLine(
        currentTimeInMillis: Long,
        newPrediction: Line?
    ): LineWithProbability? {
        removeExpiredElementsFromMemorySynchronized(currentTimeInMillis)
        if (newPrediction != null) {
            saveNewElementInMemorySynchronised(newPrediction, currentTimeInMillis)
        }
        return calculatedResultWithProbability()
    }

    private fun calculatedResultWithProbability(): LineWithProbability? {
        synchronized(this) {
            val linesWithShare = linesWithShare()
            val indexesOfDuplicatedElements = indexesOfDuplicatedElements()
            val reducedLinesWithShare = reducedLinesWithShare(
                indexesOfDuplicatedElements,
                linesWithShare
            )

            val allConditionsMet =
                thereAreMoreThanOneElement(reducedLinesWithShare) &&
                        allElementHaveTheSameValue(reducedLinesWithShare)
            return if (allConditionsMet) {
                null
            } else {
                sortedReducedLineWithProbability(reducedLinesWithShare)
            }
        }
    }

    private fun linesWithShare(): List<LineWithBufferedInfoAndShare> {
        val totalMemoryCapacity = memory.size
        val percentageShareOfEachElement = (100f / totalMemoryCapacity).toInt()
        return memory
            .map { line -> LineWithBufferedInfoAndShare(line, percentageShareOfEachElement) }
    }

    private fun indexesOfDuplicatedElements(): List<LineWithBufferedInfoAndIndexes> =
        memory.map { line -> LineWithBufferedInfoAndIndexes(line, indexesFound(line)) }

    private fun reducedLinesWithShare(
        indexesOfDuplicatedElements: List<LineWithBufferedInfoAndIndexes>,
        linesWithShare: List<LineWithBufferedInfoAndShare>
    ): Map<Line, Int> {
        val reducedLinesWithShare = mutableMapOf<Line, Int>()
        indexesOfDuplicatedElements
            .forEach { element ->
                val lineWithBufferedInfo = element.lineWithBufferedInfo
                val indexes = element.indexes
                var totalSumOfShareForElement = 0
                indexes.forEach { index ->
                    linesWithShare.getOrElse(index) { null }?.let { lineWithShare ->
                        totalSumOfShareForElement += lineWithShare.share
                    }
                }
                reducedLinesWithShare[lineWithBufferedInfo.line] =
                    reducedLinesWithShare.getOrElse(lineWithBufferedInfo.line) { 0 } + totalSumOfShareForElement
            }
        return reducedLinesWithShare
    }

    private fun thereAreMoreThanOneElement(reducedLinesWithShare: Map<Line, Int>): Boolean =
        reducedLinesWithShare.size > 1

    private fun allElementHaveTheSameValue(reducedLinesWithShare: Map<Line, Int>): Boolean =
        reducedLinesWithShare.entries.run {
            val shareOfFirstElement = firstOrNull()?.value
            all { it.value == shareOfFirstElement }
        }

    private fun sortedReducedLineWithProbability(
        reducedLinesWithShare: Map<Line, Int>
    ): LineWithProbability? =
        reducedLinesWithShare
            .entries
            .sortedByDescending { it.value }
            .map {
                val line = it.key
                val probability = it.value
                LineWithProbability(line, probability)
            }
            .firstOrNull()

    private fun indexesFound(line: LineWithBufferedInfo): List<Int> {
        val indexes = mutableListOf<Int>()
        memory.forEachIndexed { index, filteredLine ->
            if (line == filteredLine) {
                indexes.add(index)
            }
        }
        return indexes
    }

    private fun saveNewElementInMemorySynchronised(line: Line, insertionTimestampInMillis: Long) {
        synchronized(this) {
            val newElement = LineWithBufferedInfo(line, insertionTimestampInMillis)
            memory.add(newElement)
        }
    }

    private fun removeExpiredElementsFromMemorySynchronized(currentTimeInMillis: Long) {
        synchronized(this) {
            val iterator = memory.iterator()
            while (iterator.hasNext()) {
                val line = iterator.next()
                if (isMemoryExpiredNew(currentTimeInMillis, line.insertionTimestampInMillis)) {
                    iterator.remove()
                }
            }
        }
    }

    private fun isMemoryExpiredNew(
        currentTimeInMillis: Long,
        lineFromMemoryTimestampInMillis: Long
    ): Boolean =
        (currentTimeInMillis - lineFromMemoryTimestampInMillis) >= LIFETIME_ELEMENT_IN_MEMORY_IN_MILLIS
}

private data class LineWithBufferedInfoAndShare(
    val lineWithBufferedInfo: LineWithBufferedInfo,
    val share: Int
)

private data class LineWithBufferedInfoAndIndexes(
    val lineWithBufferedInfo: LineWithBufferedInfo,
    val indexes: List<Int>
)

private data class LineWithBufferedInfo(
    val line: Line,
    val insertionTimestampInMillis: Long
)