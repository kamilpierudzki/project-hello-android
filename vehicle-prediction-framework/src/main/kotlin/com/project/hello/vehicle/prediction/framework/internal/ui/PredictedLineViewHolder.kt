package com.project.hello.vehicle.prediction.framework.internal.ui

import androidx.recyclerview.widget.RecyclerView
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.prediction.framework.databinding.PredictedItemBinding

internal class PredictedLineViewHolder(private val viewBinding: PredictedItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun setUpView(data: LineWithProbability) {
        val lineNumber = data.line.number

        viewBinding.predictedNumber.text = lineNumber
        viewBinding.predictedProbability.text = "${data.probability}%"
        viewBinding.root.contentDescription = lineNumber
    }
}