package com.project.hello.vehicle.prediction.framework.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.hello.vehicle.domain.FpsCounter
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
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