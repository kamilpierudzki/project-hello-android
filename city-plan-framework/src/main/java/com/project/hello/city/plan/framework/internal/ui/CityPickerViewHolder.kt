package com.project.hello.city.plan.framework.internal.ui

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.framework.R
import com.project.hello.city.plan.framework.databinding.CityItemBinding

internal class CityPickerViewHolder(private val viewBinding: CityItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    @RequiresApi(Build.VERSION_CODES.DONUT)
    fun setupView(city: City, selectionListener: () -> Unit) {
        val cityName = city.cityPlan.city
        viewBinding.city.text = cityName
        viewBinding.selected.visibility = if (city.selected) View.VISIBLE else View.GONE
        val contentDescription = if (city.selected) {
            val context = viewBinding.selected.context
            val textSelectedFromRes = context.getString(R.string.currently_selected)
            "${cityName}, $textSelectedFromRes"
        } else {
            cityName
        }
        viewBinding.root.contentDescription = contentDescription
        viewBinding.root.setOnClickListener {
            selectionListener.invoke()
        }
    }
}

internal data class City(val cityPlan: CityPlan, val selected: Boolean)