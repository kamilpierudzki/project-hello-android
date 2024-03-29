package com.project.hello.vehicle.prediction.internal.camera

import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import com.project.hello.vehicle.prediction.internal.textrecognition.DisposableImageAnalyzer
import java.util.concurrent.Executor
import java.util.concurrent.Executors

internal class CameraAnalysis(private val fragment: Fragment) {

    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(fragment.requireContext())

    @SuppressLint("NewApi", "UnsafeExperimentalUsageError")
    fun startCameraAnalysis(
        imageAnalyzer: DisposableImageAnalyzer,
        surfaceProvider: Preview.SurfaceProvider,
    ) {
        cameraProviderFuture.addListener({
            val imageAnalysisUseCase = createImageAnalysisUseCase(
                createAnalysisTargetResolution(),
                imageAnalyzer
            )
            val previewUseCase = createPreviewUseCase(
                createPreviewTargetResolution(),
                surfaceProvider
            )

            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                bindCameraProviderToLifecycle(
                    cameraProvider,
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
//        .setTargetResolution(targetResolution)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply {
            setAnalyzer(createWorkerExecutor(), imageAnalyzer)
        }

    private fun createWorkerExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    private fun createPreviewTargetResolution(): Size = Size(1280, 720)

    private fun createAnalysisTargetResolution(): Size = Size(1280, 720)

    private fun createPreviewUseCase(
        targetResolution: Size,
        surfaceProvider: Preview.SurfaceProvider
    ) = Preview.Builder()
//        .setTargetResolution(targetResolution)
        .build()
        .apply {
            setSurfaceProvider(surfaceProvider)
        }

    private fun bindCameraProviderToLifecycle(
        cameraProvider: ProcessCameraProvider,
        imageAnalysisUseCase: ImageAnalysis,
        previewUseCase: Preview
    ) {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            fragment,
            CameraSelector.DEFAULT_BACK_CAMERA,
            imageAnalysisUseCase,
            previewUseCase
        )
    }
}