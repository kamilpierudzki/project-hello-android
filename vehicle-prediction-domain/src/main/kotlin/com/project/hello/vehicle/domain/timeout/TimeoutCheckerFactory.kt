package com.project.hello.vehicle.domain.timeout

interface TimeoutCheckerFactory {
    fun create(): TimeoutChecker
}