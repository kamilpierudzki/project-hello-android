package com.project.hello.city.plan.framework.internal.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.framework.R
import com.project.hello.city.plan.framework.databinding.CityItemBinding

internal class CityViewHolder(private val viewBinding: CityItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun setupView(city: CityPickerRow.City, selectionListener: () -> Unit) {
        viewBinding.city.text = city.cityPlan.city
        viewBinding.lastUpdate.text = city.cityPlan.lastUpdateDate
        viewBinding.selected.visibility = if (city.selected) View.VISIBLE else View.GONE
        viewBinding.root.setOnClickListener {
            selectionListener.invoke()
        }
        updateContentDescription(city)
    }

    private fun updateContentDescription(city: CityPickerRow.City) {
        val context = viewBinding.selected.context
        val cityName = city.cityPlan.city
        val lastUpdateValue = city.cityPlan.lastUpdateDate
        val lastUpdateLabel = context.getString(R.string.city_item_last_update_label)

        val selectionText = if (city.selected) {
            "${context.getString(R.string.currently_selected)}"
        } else {
            ""
        }
        val contentDescription = "$cityName, $lastUpdateLabel $lastUpdateValue, $selectionText"
        viewBinding.root.contentDescription = contentDescription
    }
}