package com.project.hello.vehicle.prediction.framework.internal.ui

import androidx.recyclerview.widget.RecyclerView
import com.project.hello.vehicle.domain.analysis.LineWithAccuracyAndProbability
import com.project.hello.vehicle.prediction.framework.databinding.PredictedItemBinding

internal class PredictedLineViewHolder(private val viewBinding: PredictedItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun setUpView(position: Int, data: List<LineWithAccuracyAndProbability>) {
        val lineNumber = data[position].lineWithAccuracy.line.number
        val direction = data[position].lineWithAccuracy.line.destination
        viewBinding.predictedNumber.text = lineNumber
        viewBinding.predictedDirection.text =
            direction.takeIf { isMoreThanOneNumberInCollection(it, data) } ?: "-"
        viewBinding.predictedProbability.text = "${data[position].probability}"
        viewBinding.root.contentDescription = "$lineNumber, $direction"
    }

    private fun isMoreThanOneNumberInCollection(
        number: String,
        data: List<LineWithAccuracyAndProbability>
    ): Boolean {
        val count = data.count {
            number == it.lineWithAccuracy.line.number
        }
        return count > 1
    }
}