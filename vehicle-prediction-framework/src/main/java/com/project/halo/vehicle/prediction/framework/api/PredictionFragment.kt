package com.project.halo.vehicle.prediction.framework.api

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.project.hallo.city.plan.domain.Line
import com.project.halo.commons.viewmodel.ExternalViewModelProvider
import com.project.halo.commons.viewmodel.ViewModelProvider
import com.project.halo.commons.viewmodel.ViewModelType
import com.project.halo.commons.viewmodel.externalViewModels
import com.project.halo.vehicle.prediction.framework.databinding.PredictionFragmentBinding
import com.project.halo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class PredictionFragment : Fragment() {

    private var _binding: PredictionFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    @ViewModelProvider(ViewModelType.FRAGMENT)
    lateinit var predictionViewModelProvider: ExternalViewModelProvider<PredictionViewModel>

    private val predictionViewModel by externalViewModels {
        predictionViewModelProvider
    }

    @Inject
    lateinit var textAnalyzer: DisposableImageAnalyzer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PredictionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePredictedLines()
        observeRecognisedTexts()
        startCameraAnalysis()
    }

    override fun onDestroy() {
        super.onDestroy()
        textAnalyzer.dispose()
    }

    private fun observeRecognisedTexts() {
        lifecycleScope.launch {
            textAnalyzer.textsObserver.consumeAsFlow().collect {
                if (it.isNotEmpty()) {
                    android.util.Log.d("test123", "$it")
                    predictionViewModel.processRecognisedTexts(it)
                }
            }
        }
    }

    private fun observePredictedLines() {
        predictionViewModel.predictedLines.observe(viewLifecycleOwner, { lines ->
            if (lines.isNotEmpty()) {
                val lineNumbers: String = lines
                    .map { it.number }
                    .reduce { acc: String, s: String -> "$acc\n$s" }

                binding.lineNumber.text = lineNumbers
            }
        })
    }

    @SuppressLint("NewApi", "UnsafeExperimentalUsageError")
    private fun startCameraAnalysis() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val analysisExecutor = Executors.newFixedThreadPool(4)
            val targetResolution = Size(1280, 720)

            val imageAnalysisUseCase = ImageAnalysis.Builder()
                .setTargetResolution(targetResolution)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .apply {
                    setAnalyzer(analysisExecutor, textAnalyzer)
                }

            val previewUseCase = Preview.Builder()
                .setTargetResolution(targetResolution)
                .build()
                .apply {
                    setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    imageAnalysisUseCase,
                    previewUseCase
                )
            } catch (exc: Exception) {
            }

        }, ContextCompat.getMainExecutor(requireActivity()))
    }
}