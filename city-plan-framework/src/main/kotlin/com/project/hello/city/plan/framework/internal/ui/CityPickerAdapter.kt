package com.project.hello.city.plan.framework.internal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.framework.databinding.CityItemBinding
import com.project.hello.city.plan.framework.databinding.InfoRowItemBinding
import com.project.hello.commons.framework.livedata.Event
import javax.inject.Inject

internal class CityPickerAdapter @Inject constructor() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var pickerData = mutableListOf<CityPickerRow>()

    private val _citySelection = MutableLiveData<CityPlan>()
    val citySelection: LiveData<CityPlan> = _citySelection

    private val _infoSelection = MutableLiveData<Event<Unit>>()
    val infoSelection: LiveData<Event<Unit>> = _infoSelection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            CityPickerRow.RowViewType.CITY.type -> {
                val binding = CityItemBinding.inflate(inflater, parent, false)
                return CityViewHolder(binding)
            }
            CityPickerRow.RowViewType.INFO.type -> {
                val binding = InfoRowItemBinding.inflate(inflater, parent, false)
                return InfoViewHolder(binding)
            }
        }
        throw IllegalStateException("View type didn't match")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = pickerData[position]
        when (row.rowViewType) {
            CityPickerRow.RowViewType.CITY -> {
                (row as? CityPickerRow.City)?.let { city ->
                    (holder as? CityViewHolder)?.setupView(city) {
                        _citySelection.value = (row as? CityPickerRow.City)?.cityPlan
                    }
                }
            }
            CityPickerRow.RowViewType.INFO -> {
                (holder as? InfoViewHolder)?.setupView {
                    _infoSelection.value = Event(Unit)
                }
            }
        }
    }

    override fun getItemCount(): Int = pickerData.size

    override fun getItemViewType(position: Int): Int {
        return pickerData[position].rowViewType.type
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSupportedCities(data: List<CityPickerRow>) {
        pickerData.clear()
        pickerData = data.toMutableList().apply {
            add(CityPickerRow.Info)
        }
        notifyDataSetChanged()
    }
}

internal sealed class CityPickerRow(val rowViewType: RowViewType) {
    data class City(val cityPlan: CityPlan, val selected: Boolean) : CityPickerRow(RowViewType.CITY)
    object Info : CityPickerRow(RowViewType.INFO)

    enum class RowViewType(val type: Int) {
        CITY(0),
        INFO(1)
    }
}