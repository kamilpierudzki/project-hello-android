package com.project.hello.vehicle.prediction.timeout

interface TimeoutCheckerFactory {
    fun create(): TimeoutChecker
}