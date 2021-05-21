package com.project.hallo.city.plan.framework.internal.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.framework.databinding.CityItemBinding
import javax.inject.Inject

internal class CityPickerAdapter @Inject constructor() :
    RecyclerView.Adapter<CityPickerViewHolder>() {

    private var cities: List<City> = emptyList()

    private val _citySelection = MutableLiveData<CityPlan>()
    val citySelection: LiveData<CityPlan> = _citySelection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityPickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CityItemBinding.inflate(inflater, parent, false)
        return CityPickerViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.DONUT)
    override fun onBindViewHolder(holder: CityPickerViewHolder, position: Int) {
        val city = cities[position]
        holder.setupView(city) {
            _citySelection.value = city.cityPlan
        }
    }

    override fun getItemCount(): Int = cities.size

    fun updateData(cities: List<City>) {
        this.cities = cities
        notifyDataSetChanged()
    }
}