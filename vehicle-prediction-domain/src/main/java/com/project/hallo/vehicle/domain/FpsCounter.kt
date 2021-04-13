package com.project.hallo.vehicle.domain

class FpsCounter(private val fpsCallback: (Int) -> Unit) {

    private var counter = 0
    private var latestTimestampInMillis: Long? = null

    fun newFrameProcessed(timestampInMillis: Long) {
        if (latestTimestampInMillis == null) {
            latestTimestampInMillis = timestampInMillis
        }

        val isNewSecond = latestTimestampInMillis!! + 1_000 <= timestampInMillis
        if (isNewSecond) {
            fpsCallback(counter)
            counter = 1
            latestTimestampInMillis = timestampInMillis
        } else {
            counter++
        }
    }
}