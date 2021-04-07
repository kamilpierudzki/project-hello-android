package com.project.halo.vehicle.prediction.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.project.halo.commons.viewmodel.ExternalViewModelProvider
import com.project.halo.commons.viewmodel.ViewModelProvider
import com.project.halo.commons.viewmodel.ViewModelType
import com.project.halo.commons.viewmodel.externalViewModels
import com.project.halo.vehicle.prediction.framework.databinding.PredictionFragmentBinding
import com.project.halo.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.halo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
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

    @Inject
    lateinit var cameraAnalysis: CameraAnalysis

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
        cameraAnalysis.startCameraAnalysis(textAnalyzer, binding.cameraPreview.surfaceProvider)
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
                    .map {
                        "${it.line.number}  ${it.probability}"
                    }
                    .reduce { acc: String, s: String -> "$acc\n$s" }

                binding.lineNumber.text = lineNumbers
            }
        })
    }
}