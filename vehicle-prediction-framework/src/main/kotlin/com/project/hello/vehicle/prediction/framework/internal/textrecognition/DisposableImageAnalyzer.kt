package com.project.hello.vehicle.prediction.framework.internal.textrecognition

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.LiveData

interface DisposableImageAnalyzer : ImageAnalysis.Analyzer {
    val recognisedTexts: LiveData<List<String>>

    fun dispose()
}