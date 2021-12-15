package com.project.hello.vehicle.prediction.internal.ui

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.project.hello.transit.agency.model.Line
import javax.inject.Inject

internal class DebugCityLinesInfoImpl @Inject constructor() : CityLinesInfo {

    private val cityLinesInfo = MutableLiveData<List<Line>>()

    override fun updateCityLinesInfo(cityLines: List<Line>) {
        cityLinesInfo.value = cityLines
    }

    override fun observeCityLinesInfoUiChanges(
        viewLifecycleOwner: LifecycleOwner,
        label: TextView
    ) {
        label.visibility = View.VISIBLE
        cityLinesInfo.observe(viewLifecycleOwner, { lines ->
            val data = lines.map { it.number }.distinct()
            label.text = "(${data.size}), $data"
        })
    }
}