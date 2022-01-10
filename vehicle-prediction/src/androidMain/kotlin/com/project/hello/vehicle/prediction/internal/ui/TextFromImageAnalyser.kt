package com.project.hello.vehicle.prediction.internal.ui

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.project.hello.vehicle.prediction.internal.logger.PredictionConsoleLogger
import com.project.hello.vehicle.prediction.internal.textrecognition.DisposableImageAnalyzer
import javax.inject.Inject

internal class TextFromImageAnalyser @Inject constructor(
    private val predictionConsoleLogger: PredictionConsoleLogger
) : DisposableImageAnalyzer {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override val recognisedTexts = MutableLiveData<List<String>>()

    override fun analyze(imageProxy: ImageProxy) {
        recognizeText(imageProxy)
    }

    override fun dispose() {
        textRecognizer.close()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun recognizeText(imageProxy: ImageProxy) {
        predictionConsoleLogger.logAnalysedResolution(imageProxy.width, imageProxy.height)
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
            textRecognizer.process(image)
                .addOnSuccessListener {
                    imageProxy.close()
                    val texts: List<String> = it.textBlocks.map { textLine -> textLine.text }
                    postTexts(texts)
                }
                .addOnFailureListener {
                    imageProxy.close()
                }
        }
    }

    private fun postTexts(texts: List<String>) {
        recognisedTexts.postValue(texts)
    }
}