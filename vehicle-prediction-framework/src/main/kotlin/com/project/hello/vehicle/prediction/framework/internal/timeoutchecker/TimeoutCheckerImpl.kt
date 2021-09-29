package com.project.hello.vehicle.prediction.framework.internal.timeoutchecker

import com.project.hello.vehicle.domain.steps.TimeoutChecker

private const val TIMEOUT_IN_MILLIS = 5_000

internal class TimeoutCheckerImpl: TimeoutChecker {

    private val startTimeInMillis: Long = System.currentTimeMillis()

    override fun isTimeout(): Boolean {
        val currentTimeInMillis = System.currentTimeMillis()
        return startTimeInMillis + TIMEOUT_IN_MILLIS < currentTimeInMillis
    }

    /*
    startTimeInMillis (1_000) + TIMEOUT_IN_MILLIS (5_000) = 6_000
    6_000 < 5_500 = false (NO_TIMEOUT)
    6_000 < 6_500 = true (TIMEOUT)
    */
}