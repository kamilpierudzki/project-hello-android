package com.project.hello.vehicle.prediction.framework.internal.ui

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.project.hello.transit.agency.domain.model.Line
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
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
            label.text = "(${lines.size}), ${lines.map { it.number }.distinct()}"
        })
    }
}