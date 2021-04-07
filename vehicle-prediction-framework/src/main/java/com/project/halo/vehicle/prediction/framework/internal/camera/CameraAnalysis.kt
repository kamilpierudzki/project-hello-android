package com.project.halo.vehicle.prediction.framework.internal.camera

import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.project.halo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import java.util.concurrent.Executors

class CameraAnalysis(
    private val fragment: Fragment
) {

    @SuppressLint("NewApi", "UnsafeExperimentalUsageError")
    fun startCameraAnalysis(
        imageAnalyzer: DisposableImageAnalyzer,
        surfaceProvider: Preview.SurfaceProvider,
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(fragment.requireContext())
        cameraProviderFuture.addListener({
            val analysisExecutor = Executors.newSingleThreadExecutor()
            val targetResolution = Size(1280, 720)

            val imageAnalysisUseCase = ImageAnalysis.Builder()
                .setTargetResolution(targetResolution)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .apply {
                    setAnalyzer(analysisExecutor, imageAnalyzer)
                }

            val previewUseCase = Preview.Builder()
                .setTargetResolution(targetResolution)
                .build()
                .apply {
                    setSurfaceProvider(surfaceProvider)
                }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    fragment,
                    cameraSelector,
                    imageAnalysisUseCase,
                    previewUseCase
                )
            } catch (exc: Exception) {
            }

        }, ContextCompat.getMainExecutor(fragment.requireContext()))
    }
}