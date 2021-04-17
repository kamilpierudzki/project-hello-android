package com.project.hallo.city.plan.framework.internal.ui

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.project.hallo.city.plan.framework.R
import com.project.hallo.city.plan.framework.databinding.CityItemBinding

internal class CityPickerViewHolder(private val viewBinding: CityItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    @RequiresApi(Build.VERSION_CODES.DONUT)
    fun setupView(city: City) {
        viewBinding.city.text = city.name
        viewBinding.selected.visibility = if (city.selected) View.VISIBLE else View.GONE
        val contentDescription = if (city.selected) {
            val context = viewBinding.selected.context
            val textSelectedFromRes = context.getString(R.string.currently_selected)
            "${city.name}, $textSelectedFromRes"
        } else {
            city.name
        }
        viewBinding.root.contentDescription = contentDescription
    }
}

internal data class City(val name: String, val selected: Boolean)