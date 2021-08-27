package com.project.hello.vehicle.prediction.framework.internal.camera

import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import com.project.hello.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.math.min

class CameraAnalysis(
    private val fragment: Fragment
) {

    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(fragment.requireContext())

    @SuppressLint("NewApi", "UnsafeExperimentalUsageError")
    fun startCameraAnalysis(
        imageAnalyzer: DisposableImageAnalyzer,
        surfaceProvider: Preview.SurfaceProvider,
    ) {
        cameraProviderFuture.addListener({
            val cameraSelector = createCameraSelector()
            val targetResolution = createTargetResolution()
            val imageAnalysisUseCase = createImageAnalysisUseCase(targetResolution, imageAnalyzer)
            val previewUseCase = createPreviewUseCase(targetResolution, surfaceProvider)

            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                bindCameraProviderToLifecycle(
                    cameraProvider,
                    cameraSelector,
                    imageAnalysisUseCase,
                    previewUseCase
                )
            } catch (exc: Exception) {
            }

        }, ContextCompat.getMainExecutor(fragment.requireContext()))
    }

    private fun createImageAnalysisUseCase(
        targetResolution: Size,
        imageAnalyzer: DisposableImageAnalyzer
    ) = ImageAnalysis.Builder()
        .setTargetResolution(targetResolution)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply {
            setAnalyzer(createWorkerExecutor(), imageAnalyzer)
        }

    private fun createWorkerExecutor(): Executor {
        val availableProcessors = Runtime.getRuntime().availableProcessors()
        return Executors.newFixedThreadPool(min(2, availableProcessors))
    }

    private fun createTargetResolution(): Size = Size(1280, 720)

    private fun createPreviewUseCase(
        targetResolution: Size,
        surfaceProvider: Preview.SurfaceProvider
    ) = Preview.Builder()
        .setTargetResolution(targetResolution)
        .build()
        .apply {
            setSurfaceProvider(surfaceProvider)
        }

    private fun createCameraSelector() =
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

    private fun bindCameraProviderToLifecycle(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector,
        imageAnalysisUseCase: ImageAnalysis,
        previewUseCase: Preview
    ) {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            fragment,
            cameraSelector,
            imageAnalysisUseCase,
            previewUseCase
        )
    }
}