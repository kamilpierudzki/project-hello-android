package com.project.hallo.vehicle.prediction.framework.api

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hallo.city.plan.domain.VehicleData
import com.project.hallo.city.plan.framework.internal.datamodel.VehicleDataParcelable
import com.project.hallo.country.api.ResourceCountryCharacters
import com.project.hallo.vehicle.prediction.framework.R
import com.project.hallo.vehicle.prediction.framework.databinding.PredictionFragmentBinding
import com.project.hallo.vehicle.prediction.framework.internal.FpsCounterWrapper
import com.project.hallo.vehicle.prediction.framework.internal.PredictionViewModel
import com.project.hallo.vehicle.prediction.framework.internal.PredictionViewModelInitialData
import com.project.hallo.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.hallo.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import com.project.hallo.vehicle.prediction.framework.internal.ui.PredictedLinesAdapter
import com.project.hallo.vehicle.prediction.framework.internal.ui.showNeutralDialog
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

    @Inject
    lateinit var resourceCountryCharacters: ResourceCountryCharacters

    private val sageArgs: PredictionFragmentArgs by navArgs()
    private val initialVehicleData: VehicleDataParcelable get() = sageArgs.vehicleDataParcelable
    private lateinit var binding: PredictionFragmentBinding
    private lateinit var predictedLinesAdapter: PredictedLinesAdapter
    private val predictionViewModel: PredictionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PredictionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        passInitialInfoToViewModel()
        observePredictedLines()
        observeRecognisedTexts()
        observeFpsCounter()
        observeScreenContentDescription()
        startCameraAnalysis()
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
            showNeutralDialog(it.context, R.string.help_dialog_title, R.string.help_dialog_message)
        }
    }

    private fun observeRecognisedTexts() {
        textAnalyzer.textsObserver.observe(viewLifecycleOwner, { texts ->
            if (texts.isNotEmpty()) {
                predictionViewModel.processRecognisedTexts(texts)
            }
        })
    }

    private fun passInitialInfoToViewModel() {
        val initialData = PredictionViewModelInitialData(
            targetVehicleTypes = initialVehicleData.vehicleTypes,
            countryCharacters = resourceCountryCharacters.get()
        )
        predictionViewModel.setTargetVehicleType(initialData)
    }

    private fun observePredictedLines() {
        predictionViewModel.predictedLines.observe(viewLifecycleOwner, { lines ->
            if (lines.isNotEmpty()) {
                predictedLinesAdapter.updateData(lines)
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

    private fun startCameraAnalysis() {
        cameraAnalysis.startCameraAnalysis(textAnalyzer, binding.cameraPreview.surfaceProvider)
    }
}