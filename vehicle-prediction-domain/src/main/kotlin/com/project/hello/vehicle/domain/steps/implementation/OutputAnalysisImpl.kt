package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.vehicle.domain.steps.AccuracyLevel
import com.project.hello.vehicle.domain.steps.LineWithAccuracy
import com.project.hello.vehicle.domain.steps.OutputAnalysis

class OutputAnalysisImpl : OutputAnalysis {

    private val priorities = listOf(
        AccuracyLevel.NUMBER_MATCHED,
        AccuracyLevel.NUMBER_SLICE,
        AccuracyLevel.DESTINATION_MATCHED,
        AccuracyLevel.DESTINATION_SLICE
    )

    override fun analysedOutputMatrix(outputMatrix: List<List<LineWithAccuracy>>): List<LineWithAccuracy> {
        val elementWithHighestAccuracy = getInfoOfElementWithHighestAccuracy(outputMatrix)

        val maxNumberOfRepetitions = elementWithHighestAccuracy.maxNumberOfRepetitions
        return elementWithHighestAccuracy.analysed.entries
            .asSequence()
            .filter { it.value == maxNumberOfRepetitions }
            .map { it.key }
            .toList()
    }

    private fun getInfoOfElementWithHighestAccuracy(outputs: List<List<LineWithAccuracy>>): AccuracyAnalysisInfo {
        for (priority in priorities) {
            val analysisData = createAnalysisForAccuracy(priority, outputs)
            if (analysisData.anyFound) {
                return analysisData
            }
        }
        return AccuracyAnalysisInfo.EMPTY
    }

    private fun createAnalysisForAccuracy(
        accuracyLevel: AccuracyLevel,
        outputs: List<List<LineWithAccuracy>>
    ): AccuracyAnalysisInfo {
        val bucket: MutableMap<LineWithAccuracy, Int> = mutableMapOf()
        var maxNumberOfRepetitions = 0
        for (rows: List<LineWithAccuracy> in outputs) {
            for (element: LineWithAccuracy in rows) {
                if (element.accuracyLevel == accuracyLevel) {
                    val elementCount = bucket.getOrElse(element, { 0 }) + 1
                    bucket[element] = elementCount

                    if (elementCount > maxNumberOfRepetitions) {
                        maxNumberOfRepetitions = elementCount
                    }
                }
            }
        }
        return AccuracyAnalysisInfo(bucket, maxNumberOfRepetitions)
    }
}

private data class AccuracyAnalysisInfo(
    val analysed: Map<LineWithAccuracy, Int>,
    val maxNumberOfRepetitions: Int
) {

    val anyFound = analysed.isNotEmpty()

    companion object {
        val EMPTY = AccuracyAnalysisInfo(emptyMap(), 0)
    }
}