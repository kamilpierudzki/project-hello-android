package com.project.hello.vehicle.prediction.framework.internal.ui

import androidx.recyclerview.widget.RecyclerView
import com.project.hello.vehicle.domain.analysis.LineWithAccuracyAndProbability
import com.project.hello.vehicle.prediction.framework.databinding.PredictedItemBinding

internal class PredictedLineViewHolder(private val viewBinding: PredictedItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun setUpView(data: LineWithAccuracyAndProbability) {
        val lineNumber = data.lineWithAccuracy.line.number
        val direction = data.lineWithAccuracy.line.destination
        viewBinding.predictedNumber.text = lineNumber
        viewBinding.predictedDirection.text = direction
        viewBinding.predictedProbability.text = "${data.probability}"
        viewBinding.root.contentDescription = "$lineNumber, $direction"
    }
}