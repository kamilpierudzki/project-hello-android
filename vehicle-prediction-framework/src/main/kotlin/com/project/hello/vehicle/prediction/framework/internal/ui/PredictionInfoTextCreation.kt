package com.project.hello.vehicle.prediction.framework.internal.ui

import com.project.hello.commons.framework.ui.IText
import com.project.hello.commons.framework.ui.Text
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.prediction.framework.R
import javax.inject.Inject

internal class PredictionInfoTextCreation @Inject constructor() {

    fun createTextForConfidenceLabel(lineWithProbability: LineWithProbability) =
        PredictionLabelInfo(
            text = createConfidenceLabelText(lineWithProbability),
            contentDescription = createConfidenceLabelContentDescription(lineWithProbability)
        )

    fun createTextForNumberLabel(lineWithProbability: LineWithProbability): PredictionLabelInfo {
        val text = createNumberLabel(lineWithProbability)
        return PredictionLabelInfo(
            text = text,
            contentDescription = text,
            announceForAccessibility = text
        )
    }

    private fun createConfidenceLabelText(lineWithProbability: LineWithProbability): IText =
        Text.of(
            listOf(
                Text.of("${lineWithProbability.probability}"),
                Text.of("%")
            ), separator = ""
        )

    private fun createConfidenceLabelContentDescription(lineWithProbability: LineWithProbability): IText =
        Text.of(
            listOf(
                Text.of(
                    listOf(
                        Text.of(R.string.line),
                        Text.of(lineWithProbability.line.number)
                    ), separator = " "
                ),
                Text.of(
                    listOf(
                        Text.of(R.string.probably),
                        Text.of(
                            listOf(
                                Text.of("${lineWithProbability.probability}"),
                                Text.of(R.string.percent)
                            ), separator = ""
                        )
                    ), separator = " "
                )
            ),
            separator = ", "
        )

    private fun createNumberLabel(lineWithProbability: LineWithProbability): IText =
        Text.of(lineWithProbability.line.number)
}