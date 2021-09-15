package com.project.hello.vehicle.prediction.framework.api

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.framework.api.CityPickViewModel
import com.project.hello.city.plan.framework.internal.datamodel.VehicleDataParcelable
import com.project.hello.commons.framework.ui.showBinaryDialog
import com.project.hello.commons.framework.ui.showInformationDialog
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.commons.framework.viewmodel.externalViewModels
import com.project.hello.country.api.ResourceCountryCharacters
import com.project.hello.vehicle.prediction.framework.R
import com.project.hello.vehicle.prediction.framework.databinding.PredictionFragmentBinding
import com.project.hello.vehicle.prediction.framework.internal.FpsCounterWrapper
import com.project.hello.vehicle.prediction.framework.internal.PredictionViewModel
import com.project.hello.vehicle.prediction.framework.internal.PredictionViewModelInitialData
import com.project.hello.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.hello.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class PredictionFragment : Fragment() {

    @Inject
    lateinit var textAnalyzer: DisposableImageAnalyzer

    @Inject
    lateinit var cameraAnalysis: CameraAnalysis

    @Inject
    lateinit var fpsCounterWrapper: FpsCounterWrapper

    @Inject
    lateinit var resourceCountryCharacters: ResourceCountryCharacters

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var cityPickViewModelProvider: ExternalViewModelProvider<CityPickViewModel>

    private val cityPickViewModel by externalViewModels {
        cityPickViewModelProvider
    }

    private val safeArgs: PredictionFragmentArgs by navArgs()
    private val initialVehicleData: VehicleDataParcelable get() = safeArgs.vehicleDataParcelable
    private lateinit var binding: PredictionFragmentBinding
    private val predictionViewModel: PredictionViewModel by viewModels()
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>

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
        announceScreenNameByScreenReader()
        setupViews()
        passInitialInfoToViewModel()
        observeRecognisedTexts()
        observeFpsCounter()
        observeNewFrameEvent()
        observePredictedNumber()
        observePredictedConfidenceInfo()
        initRequestCameraPermissionLauncher()
        processPermissionLogic()
    }

    override fun onDestroy() {
        super.onDestroy()
        textAnalyzer.dispose()
    }

    private fun announceScreenNameByScreenReader() {
        binding.root.announceForAccessibility(getString(R.string.prediction_fragment_label))
    }

    private fun setupViews() {
        setupHelpIconClicks()
    }

    private fun setupHelpIconClicks() {
        binding.helpIcon.setOnClickListener {
            showInformationDialog(
                it.context,
                R.string.help_dialog_title,
                R.string.help_dialog_message
            )
        }
    }

    private fun observeRecognisedTexts() {
        textAnalyzer.recognisedTexts.observe(viewLifecycleOwner, { texts ->
            if (texts.isNotEmpty()) {
                predictionViewModel.processRecognisedTexts(texts)
            }
        })
    }

    private fun passInitialInfoToViewModel() {
        val currentlySelectedCity: CityPlan? = cityPickViewModel.currentlySelectedCity
        if (currentlySelectedCity != null) {
            val initialData = PredictionViewModelInitialData(
                targetVehicleTypes = initialVehicleData.vehicleTypes,
                countryCharacters = resourceCountryCharacters.get(),
                selectedCity = currentlySelectedCity
            )
            predictionViewModel.setInitialData(initialData)
        }
    }

    private fun observeNewFrameEvent() {
        predictionViewModel.newFrame.observe(viewLifecycleOwner, {
            fpsCounterWrapper.newFrameProcessed(System.currentTimeMillis())
        })
    }

    private fun observePredictedNumber() {
        predictionViewModel.predictedNumberLabel.observe(viewLifecycleOwner, { numberInfo ->
            binding.predictedNumber.text = numberInfo.text.get(resources)
            binding.predictedNumber.contentDescription =
                numberInfo.contentDescription.get(resources)
            numberInfo.announceForAccessibility?.get(resources)?.let {
                binding.predictedNumber.announceForAccessibility(it)
            }
        })
    }

    private fun observePredictedConfidenceInfo() {
        predictionViewModel.predictedConfidenceInfo.observe(viewLifecycleOwner, { confidenceInfo ->
            binding.predictionConfidence.text = confidenceInfo.text.get(resources)
            binding.predictionConfidence.contentDescription =
                confidenceInfo.contentDescription.get(resources)
        })
    }

    private fun observeFpsCounter() {
        fpsCounterWrapper.currentValue.observe(viewLifecycleOwner, { fps ->
            binding.fpsCounter.text = "$fps"
        })
    }

    private fun initRequestCameraPermissionLauncher() {
        requestCameraPermissionLauncher = registerForActivityResult(
            RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startCameraAnalysis()
            } else {
                showExplanatoryWhyPermissionIsRequired()
            }
        }
    }

    private fun processPermissionLogic() {
        if (!isCameraPermissionGranted()) {
            requestCameraPermission()
        } else {
            startCameraAnalysis()
        }
    }

    private fun isCameraPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startCameraAnalysis() {
        cameraAnalysis.startCameraAnalysis(textAnalyzer, binding.cameraPreview.surfaceProvider)
    }

    private fun showExplanatoryWhyPermissionIsRequired() {
        showBinaryDialog(
            requireContext(),
            R.string.camera_permission_explanatory_title,
            R.string.camera_permission_explanatory_message,
            positiveAction = {
                processPermissionLogic()
            },
            negativeAction = {
                goBackToPreviousScreen()
            }
        )
    }

    private fun goBackToPreviousScreen() {
        findNavController().navigateUp()
    }
}