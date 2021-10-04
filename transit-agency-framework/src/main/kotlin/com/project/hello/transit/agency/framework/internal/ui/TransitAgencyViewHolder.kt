package com.project.hello.transit.agency.framework.internal.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.project.hello.transit.agency.framework.R
import com.project.hello.transit.agency.framework.databinding.TransitAgencyItemBinding

internal class TransitAgencyViewHolder(private val viewBinding: TransitAgencyItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun setupView(
        transitAgency: TransitAgencyPickerRow.TransitAgencyRow,
        selectionListener: () -> Unit
    ) {
        viewBinding.transitAgency.text = transitAgency.transitAgency.transitAgency
        viewBinding.lastUpdate.text = transitAgency.transitAgency.lastUpdateFormatted
        viewBinding.selected.visibility = if (transitAgency.selected) View.VISIBLE else View.GONE
        viewBinding.root.setOnClickListener {
            selectionListener.invoke()
        }
        updateContentDescription(transitAgency)
    }

    private fun updateContentDescription(transitAgency: TransitAgencyPickerRow.TransitAgencyRow) {
        val context = viewBinding.selected.context
        val transitAgencyName = transitAgency.transitAgency.trams
        val lastUpdateValue = transitAgency.transitAgency.lastUpdateFormatted
        val lastUpdateLabel = context.getString(R.string.transit_agency_item_last_update_label)

        val selectionText = if (transitAgency.selected) {
            "${context.getString(R.string.currently_selected)}"
        } else {
            ""
        }
        val contentDescription =
            "$transitAgencyName, $lastUpdateLabel $lastUpdateValue, $selectionText"
        viewBinding.root.contentDescription = contentDescription
    }
}