package com.project.hallo.vehicle.prediction.framework.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.hallo.vehicle.domain.FpsCounter
import javax.inject.Inject

class FpsCounterWrapper @Inject constructor() {

    private val fpsCounter = FpsCounter(::onFpsChanged)
    private val _currentValue = MutableLiveData(0)
    val currentValue: LiveData<Int> = _currentValue

    fun newFrameProcessed(timestampInMillis: Long) {
        fpsCounter.newFrameProcessed(timestampInMillis)
    }

    private fun onFpsChanged(fps: Int) {
        _currentValue.value = fps
    }
}