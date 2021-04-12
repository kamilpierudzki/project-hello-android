package com.project.halo.vehicle.prediction.framework.api

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.halo.vehicle.prediction.framework.databinding.PredictionFragmentBinding
import com.project.halo.vehicle.prediction.framework.internal.FpsCounterWrapper
import com.project.halo.vehicle.prediction.framework.internal.PredictionViewModel
import com.project.halo.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.halo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import com.project.halo.vehicle.prediction.framework.internal.ui.PredictedLinesAdapter
import com.project.halo.vehicle.prediction.framework.internal.ui.showHelpDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.DONUT)
@AndroidEntryPoint
class PredictionFragment : Fragment() {

    @Inject
    lateinit var textAnalyzer: DisposableImageAnalyzer

    @Inject
    lateinit var cameraAnalysis: CameraAnalysis

    @Inject
    lateinit var fpsCounterWrapper: FpsCounterWrapper

    private var _binding: PredictionFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var predictedLinesAdapter: PredictedLinesAdapter
    private val predictionViewModel: PredictionViewModel by viewModels()

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
        observeScreenContentDescription()
        cameraAnalysis.startCameraAnalysis(textAnalyzer, binding.cameraPreview.surfaceProvider)
    }

    override fun onDestroy() {
        super.onDestroy()
        textAnalyzer.dispose()
    }

    private fun setupViews() {
        setupPredictedLinesView()
        setupHelpIconClicks()
    }

    private fun setupPredictedLinesView() {
        predictedLinesAdapter = PredictedLinesAdapter()
        binding.predictedLines.also {
            it.adapter = predictedLinesAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupHelpIconClicks() {
        binding.helpIcon.setOnClickListener {
            showHelpDialog(it.context)
        }
    }

    private fun observeRecognisedTexts() {
        textAnalyzer.textsObserver.observe(viewLifecycleOwner, { texts ->
            if (texts.isNotEmpty()) {
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

    private fun observeScreenContentDescription() {
        predictionViewModel.screenContentDescription.observe(viewLifecycleOwner, {
            binding.cameraPreview.contentDescription = it.get(resources)
        })
    }
}