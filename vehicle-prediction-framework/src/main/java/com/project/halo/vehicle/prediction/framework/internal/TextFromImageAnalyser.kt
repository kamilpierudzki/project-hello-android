package com.project.halo.vehicle.prediction.framework.internal

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
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

    override val textsObserver = MutableLiveData<List<String>>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun analyze(imageProxy: ImageProxy) {
        recognizeText(imageProxy)
    }

    override fun dispose() {
        textRecognizer.close()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun recognizeText(imageProxy: ImageProxy) {
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
        textsObserver.postValue(texts)
    }
}