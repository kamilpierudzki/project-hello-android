package com.project.hallo.city.plan.framework.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.hallo.city.plan.framework.databinding.CityItemBinding

internal class CityPickerAdapter : RecyclerView.Adapter<CityPickerViewHolder>() {

    private var cities: List<String> = emptyList()

    fun updateData(cities: List<String>) {
        this.cities = cities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityPickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CityItemBinding.inflate(inflater, parent, false)
        return CityPickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityPickerViewHolder, position: Int) {
        val city = City(cities[position], true)
        holder.setupView(city)
    }

    override fun getItemCount(): Int = cities.size
}