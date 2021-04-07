package com.project.halo.vehicle.prediction.framework.internal.textrecognition

import androidx.camera.core.ImageAnalysis
import kotlinx.coroutines.channels.Channel

interface DisposableImageAnalyzer : ImageAnalysis.Analyzer {
    val textsObserver: Channel<List<String>>

    fun dispose()
}