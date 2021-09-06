package com.project.hello.vehicle.prediction.framework.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.hello.vehicle.domain.analysis.LineWithAccuracyAndProbability
import com.project.hello.vehicle.prediction.framework.databinding.PredictedItemBinding

internal class PredictedLinesAdapter : RecyclerView.Adapter<PredictedLineViewHolder>() {

    private var dataset: List<LineWithAccuracyAndProbability> = emptyList()

    fun updateData(newData: List<LineWithAccuracyAndProbability>) {
        dataset = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictedLineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PredictedItemBinding.inflate(inflater, parent, false)
        return PredictedLineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PredictedLineViewHolder, position: Int) {
        holder.setUpView(position, dataset)
    }

    override fun getItemCount(): Int = dataset.size
}