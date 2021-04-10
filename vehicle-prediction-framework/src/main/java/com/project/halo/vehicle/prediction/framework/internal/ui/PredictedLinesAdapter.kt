package com.project.halo.vehicle.prediction.framework.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.halo.vehicle.domain.analysis.LineWithProbability
import com.project.halo.vehicle.prediction.framework.databinding.PredictedItemBinding

class PredictedLinesAdapter : RecyclerView.Adapter<PredictedLineViewHolder>() {

    private var dataset: List<LineWithProbability> = emptyList()

    fun updateDataset(newDataset: List<LineWithProbability>) {
        dataset = newDataset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictedLineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PredictedItemBinding.inflate(inflater, parent, false)
        return PredictedLineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PredictedLineViewHolder, position: Int) {
        holder.setUpView(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size
}