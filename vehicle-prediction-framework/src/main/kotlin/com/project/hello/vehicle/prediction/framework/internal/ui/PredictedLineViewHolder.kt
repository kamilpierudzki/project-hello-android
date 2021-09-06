package com.project.hello.vehicle.prediction.framework.internal.ui

import androidx.recyclerview.widget.RecyclerView
import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.analysis.LineWithProbability
import com.project.hello.vehicle.prediction.framework.databinding.PredictedItemBinding

internal class PredictedLineViewHolder(private val viewBinding: PredictedItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun setUpView(position: Int, data: List<LineWithProbability>) {
        val lineWithProbability = data[position]

        viewBinding.predictedNumber.text = lineWithProbability.line.number
        viewBinding.predictedProbability.text = "${data[position].probability}"

        updatePredictedDirection(lineWithProbability.line, data)
        updateContentDescription(lineWithProbability.line, data)
    }

    private fun updatePredictedDirection(line: Line, data: List<LineWithProbability>) {
        viewBinding.predictedDirection.text = if (isMoreThanOneNumberInData(line, data)) {
            "-"
        } else {
            line.destination
        }
    }

    private fun isMoreThanOneNumberInData(
        line: Line,
        data: List<LineWithProbability>
    ): Boolean {
        val count = data.count {
            line.number == it.line.number
        }
        return count > 1
    }

    private fun updateContentDescription(line: Line, data: List<LineWithProbability>) {
        viewBinding.root.contentDescription = if (isMoreThanOneNumberInData(line, data)) {
            line.number
        } else {
            "${line.number}, ${line.destination}"
        }
    }
}