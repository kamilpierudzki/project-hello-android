package com.project.hello.vehicle.prediction.framework.internal.ui

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.project.hello.transit.agency.domain.model.Line

interface CityLinesInfo {
    fun updateCityLinesInfo(cityLines: List<Line>)
    fun observeCityLinesInfoUiChanges(viewLifecycleOwner: LifecycleOwner, label: TextView)
}