package com.project.halo.vehicle.domain.steps

import com.project.halo.vehicle.prediction.data.steps.AccuracyLevel
import com.project.halo.vehicle.prediction.data.steps.LineWithAccuracy
import com.project.halo.vehicle.prediction.data.steps.OutputAnalysis

class OutputAnalysisImpl: OutputAnalysis {

    private val priorities = listOf(
        AccuracyLevel.NUMBER_MATCHED,
        AccuracyLevel.DESTINATION_MATCHED,
        AccuracyLevel.SLICE_MATCHED
    )

    override fun analyseOutput(outputs: List<List<LineWithAccuracy>>): List<com.project.hallo.city.plan.domain.Line> {
        var winner = AccuracyAnalysisData.EMPTY
        for (priority in priorities) {
            val analysisData = createAnalysisForAccuracy(priority, outputs)
            if (analysisData.anyFound) {
                winner = analysisData
                break
            }
        }

        val maxNumberOfRepetitions = winner.maxNumberOfRepetitions
        return winner.analysed.entries
            .asSequence()
            .filter { it.value == maxNumberOfRepetitions }
            .map { it.key.line }
            .toList()
    }

    private fun createAnalysisForAccuracy(
        accuracyLevel: AccuracyLevel,
        outputs: List<List<LineWithAccuracy>>
    ): AccuracyAnalysisData {
        val bucket: MutableMap<LineWithAccuracy, Int> = mutableMapOf()
        var maxNumberOfRepetitions = 0
        for (row: List<LineWithAccuracy> in outputs) {
            for (element: LineWithAccuracy in row) {
                if (element.accuracyLevel == accuracyLevel) {
                    val elementCount = bucket.getOrDefault(element, 1) + 1
                    bucket[element] = elementCount

                    if (elementCount > maxNumberOfRepetitions) {
                        maxNumberOfRepetitions = elementCount
                    }
                }
            }
        }
        return AccuracyAnalysisData(bucket, maxNumberOfRepetitions)
    }
}

private data class AccuracyAnalysisData(
    val analysed: Map<LineWithAccuracy, Int>,
    val maxNumberOfRepetitions: Int
) {

    val anyFound = analysed.isNotEmpty()

    companion object {
        val EMPTY = AccuracyAnalysisData(emptyMap(), 0)
    }
}