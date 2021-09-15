package com.project.hello.vehicle.prediction.framework.internal.ui

import com.project.hello.commons.framework.ui.IText
import com.project.hello.commons.framework.ui.Text

data class PredictionLabelInfo(
    val text: IText,
    val contentDescription: IText,
    val announceForAccessibility: IText? = null
) {
    companion object {
        val EMPTY = PredictionLabelInfo(text = Text.empty(), contentDescription = Text.empty())
    }
}
