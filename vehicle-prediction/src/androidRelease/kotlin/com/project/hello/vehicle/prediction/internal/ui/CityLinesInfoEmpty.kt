package com.project.hello.vehicle.prediction.internal.ui

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.project.hello.transit.agency.model.Line
import javax.inject.Inject

internal class CityLinesInfoEmpty @Inject constructor(): CityLinesInfo {

    override fun updateCityLinesInfo(cityLines: List<Line>) {}

    override fun observeCityLinesInfoUiChanges(
        viewLifecycleOwner: LifecycleOwner,
        label: TextView
    ) {
    }
}