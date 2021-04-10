package com.project.halo.vehicle.prediction.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.halo.commons.viewmodel.ExternalViewModelProvider
import com.project.halo.commons.viewmodel.ViewModelProvider
import com.project.halo.commons.viewmodel.ViewModelType
import com.project.halo.commons.viewmodel.externalViewModels
import com.project.halo.vehicle.prediction.framework.databinding.PredictionFragmentBinding
import com.project.halo.vehicle.prediction.framework.internal.FpsCounterWrapper
import com.project.halo.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.halo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import com.project.halo.vehicle.prediction.framework.internal.ui.PredictedLinesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PredictionFragment : Fragment() {

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

    @Inject
    lateinit var fpsCounterWrapper: FpsCounterWrapper

    private var _binding: PredictionFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var predictedLinesAdapter: PredictedLinesAdapter

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
        setupViews()
        observePredictedLines()
        observeRecognisedTexts()
        observeFpsCounter()
        cameraAnalysis.startCameraAnalysis(textAnalyzer, binding.cameraPreview.surfaceProvider)
    }

    override fun onDestroy() {
        super.onDestroy()
        textAnalyzer.dispose()
    }

    private fun setupViews() {
        setupPredictedLinesView()
    }

    private fun setupPredictedLinesView() {
        predictedLinesAdapter = PredictedLinesAdapter()
        binding.predictedLines.also {
            it.adapter = predictedLinesAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeRecognisedTexts() {
        textAnalyzer.textsObserver.observe(viewLifecycleOwner, { texts ->
            if (texts.isNotEmpty()) {
                android.util.Log.d("test123", texts.toString())
                predictionViewModel.processRecognisedTexts(texts)
            }
        })
    }

    private fun observePredictedLines() {
        predictionViewModel.predictedLines.observe(viewLifecycleOwner, { lines ->
            if (lines.isNotEmpty()) {
                predictedLinesAdapter.updateDataset(lines)
                fpsCounterWrapper.newFrameProcessed(System.currentTimeMillis())
            }
        })
    }

    private fun observeFpsCounter() {
        fpsCounterWrapper.currentValue.observe(viewLifecycleOwner, { fps ->
            binding.fpsCounter.text = "${fps}FPS"
        })
    }
}