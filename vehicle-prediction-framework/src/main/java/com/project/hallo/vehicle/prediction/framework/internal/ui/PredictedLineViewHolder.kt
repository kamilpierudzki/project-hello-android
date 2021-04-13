package com.project.hallo.vehicle.prediction.framework.internal.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.project.hallo.vehicle.domain.analysis.LineWithProbability
import com.project.hallo.vehicle.prediction.framework.databinding.PredictedItemBinding

internal class PredictedLineViewHolder(private val viewBinding: PredictedItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    @RequiresApi(Build.VERSION_CODES.DONUT)
    fun setUpView(data: LineWithProbability) {
        val lineNumber = data.line.number
        val direction = data.line.destination
        viewBinding.predictedNumber.text = lineNumber
        viewBinding.predictedDirection.text = direction
        viewBinding.predictedProbability.text = "${data.probability}"
        viewBinding.root.contentDescription = "$lineNumber, $direction"
    }
}