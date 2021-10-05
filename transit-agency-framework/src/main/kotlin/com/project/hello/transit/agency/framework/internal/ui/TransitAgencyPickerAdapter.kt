package com.project.hello.transit.agency.framework.internal.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.project.hello.commons.framework.livedata.Event
import com.project.hello.transit.agency.domain.model.TransitAgency
import com.project.hello.transit.agency.framework.databinding.InfoRowItemBinding
import com.project.hello.transit.agency.framework.databinding.TransitAgencyItemBinding
import javax.inject.Inject

internal class TransitAgencyPickerAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var pickerData = mutableListOf<TransitAgencyPickerRow>()

    private val _transitAgencySelection = MutableLiveData<TransitAgency>()
    val transitAgencySelection: LiveData<TransitAgency> = _transitAgencySelection

    private val _infoSelection = MutableLiveData<Event<Unit>>()
    val infoSelection: LiveData<Event<Unit>> = _infoSelection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            TransitAgencyPickerRow.RowViewType.TRANSIT_AGENCY.type -> {
                val binding = TransitAgencyItemBinding.inflate(inflater, parent, false)
                return TransitAgencyViewHolder(binding)
            }
            TransitAgencyPickerRow.RowViewType.INFO.type -> {
                val binding = InfoRowItemBinding.inflate(inflater, parent, false)
                return InfoViewHolder(binding)
            }
        }
        throw IllegalStateException("View type didn't match")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = pickerData[position]
        when (row.rowViewType) {
            TransitAgencyPickerRow.RowViewType.TRANSIT_AGENCY -> {
                (row as? TransitAgencyPickerRow.TransitAgencyRow)?.let { city ->
                    (holder as? TransitAgencyViewHolder)?.setupView(city) {
                        _transitAgencySelection.value =
                            (row as? TransitAgencyPickerRow.TransitAgencyRow)?.transitAgency
                    }
                }
            }
            TransitAgencyPickerRow.RowViewType.INFO -> {
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
    fun updateSupportedCities(data: List<TransitAgencyPickerRow.TransitAgencyRow>) {
        pickerData.clear()
        pickerData.addAll(data.sortedBy { it.transitAgency.transitAgency })
        pickerData.add(TransitAgencyPickerRow.Info)
        notifyDataSetChanged()
    }
}

internal sealed class TransitAgencyPickerRow(val rowViewType: RowViewType) {
    data class TransitAgencyRow(
        val transitAgency: TransitAgency,
        val selected: Boolean
    ) : TransitAgencyPickerRow(RowViewType.TRANSIT_AGENCY)

    object Info : TransitAgencyPickerRow(RowViewType.INFO)

    enum class RowViewType(val type: Int) {
        TRANSIT_AGENCY(0),
        INFO(1)
    }
}