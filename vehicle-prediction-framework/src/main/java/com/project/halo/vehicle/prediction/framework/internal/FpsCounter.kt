package com.project.halo.vehicle.prediction.framework.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class FpsCounter @Inject constructor() {

    private var internalCounter = 0
    private val _currentValue = MutableLiveData(0)
    val currentValue: LiveData<Int> = _currentValue

    private var latestTimestampInMillis: Long? = null


    fun newFrameProcessed(timestampInMillis: Long) {
        if (latestTimestampInMillis == null) {
            latestTimestampInMillis = timestampInMillis
        }

        val isNewSecond = latestTimestampInMillis!! + 1_000 <= timestampInMillis
        if (isNewSecond) {
            _currentValue.value = internalCounter
            internalCounter = 1
            latestTimestampInMillis = timestampInMillis
        } else {
            internalCounter++
        }
    }
}