package com.project.halo.vehicle.prediction.framework.internal

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.project.halo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import javax.inject.Inject

@FragmentScoped
internal class TextFromImageAnalyser @Inject constructor() : DisposableImageAnalyzer {

    private val textRecognizer: TextRecognizer = TextRecognition.getClient()

    override val textsObserver = Channel<List<String>>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun analyze(imageProxy: ImageProxy) {
        recognizeText2(imageProxy)
    }

    override fun dispose() {
        textRecognizer.close()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun recognizeText2(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
            textRecognizer.process(image)
                .addOnSuccessListener {
                    val texts: List<String> = it.textBlocks.map { textLine -> textLine.text }
                    postTexts(texts)
                    imageProxy.close()
                }
        }
    }

    private fun postTexts(texts: List<String>) {
        GlobalScope.launch {
            textsObserver.send(texts)
        }
    }
}