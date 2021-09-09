package com.project.hello.vehicle.prediction.framework.internal.ui

import com.project.hello.commons.framework.ui.IText
import com.project.hello.commons.framework.ui.Text
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.prediction.framework.R
import javax.inject.Inject

internal class PredictionScreenContentDescription @Inject constructor() {

    fun createContentDescription(lineWithProbability: LineWithProbability?): IText =
        if (lineWithProbability != null) {
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
                            Text.of("${lineWithProbability.probability}"),
                            Text.of(R.string.percent)
                        ), separator = " "
                    )
                ),
                separator = ", "
            )
        } else {
            Text.empty()
        }
}