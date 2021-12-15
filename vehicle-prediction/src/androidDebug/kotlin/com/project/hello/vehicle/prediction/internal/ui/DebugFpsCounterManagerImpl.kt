package com.project.hello.vehicle.prediction.internal.ui

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.project.hello.vehicle.prediction.FpsCounter
import javax.inject.Inject

internal class DebugFpsCounterManagerImpl @Inject constructor() : FpsCounterManager {

    private val fpsCounter = FpsCounter(::onFpsChanged)
    private val currentValue = MutableLiveData(0)

    override fun newFrameProcessed(timestampInMillis: Long) {
        fpsCounter.newFrameProcessed(timestampInMillis)
    }

    override fun observeFpsCounterUiChanges(viewLifecycleOwner: LifecycleOwner, label: TextView) {
        label.visibility = View.VISIBLE
        currentValue.observe(viewLifecycleOwner, { fps ->
            label.text = "$fps"
        })
    }

    private fun onFpsChanged(fps: Int) {
        currentValue.value = fps
    }
}