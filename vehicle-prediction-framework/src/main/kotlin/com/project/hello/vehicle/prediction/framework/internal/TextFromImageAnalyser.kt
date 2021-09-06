package com.project.hello.vehicle.prediction.framework.internal

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.project.hello.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
internal class TextFromImageAnalyser @Inject constructor() : DisposableImageAnalyzer {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override val textsObserver = MutableLiveData<List<String>>()

    override fun analyze(imageProxy: ImageProxy) {
        recognizeText(imageProxy)
    }

    override fun dispose() {
        textRecognizer.close()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun recognizeText(imageProxy: ImageProxy) {
        android.util.Log.d("test_resolution", "width: ${imageProxy.width}, height: ${imageProxy.height}")
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
        android.util.Log.d("test_raw", "raw texts: $texts")
        textsObserver.postValue(texts)
    }
}