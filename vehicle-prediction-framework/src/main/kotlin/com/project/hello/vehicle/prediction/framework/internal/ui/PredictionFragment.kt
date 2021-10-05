package com.project.hello.vehicle.prediction.framework.internal.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import android.Manifest.permission.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.hello.transit.agency.framework.api.TransitAgencyPickViewModel
import com.project.hello.transit.agency.framework.internal.datamodel.VehicleDataParcelable
import com.project.hello.commons.framework.ui.showBinaryDialog
import com.project.hello.commons.framework.ui.showInformationDialog
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.commons.framework.viewmodel.externalViewModels
import com.project.hello.country.api.ResourceCountryCharacters
import com.project.hello.vehicle.prediction.framework.R
import com.project.hello.vehicle.prediction.framework.databinding.PredictionFragmentBinding
import com.project.hello.vehicle.prediction.framework.internal.viewmodel.PredictionViewModel
import com.project.hello.vehicle.prediction.framework.internal.viewmodel.PredictionViewModelInitialData
import com.project.hello.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.hello.vehicle.prediction.framework.internal.fps.FpsCounterManager
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
    lateinit var fpsCounterManager: FpsCounterManager

    @Inject
    lateinit var resourceCountryCharacters: ResourceCountryCharacters

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var transitAgencyPickViewModelProvider: ExternalViewModelProvider<TransitAgencyPickViewModel>

    private val cityPickViewModel by externalViewModels {
        transitAgencyPickViewModelProvider
    }

    private val safeArgs: PredictionFragmentArgs by navArgs()
    private val initialVehicleData: VehicleDataParcelable get() = safeArgs.vehicleDataParcelable
    private lateinit var binding: PredictionFragmentBinding
    private val predictionViewModel: PredictionViewModel by viewModels()
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<Array<String>>

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
        initRequestCameraPermissionLauncher()
        initRequestLocationPermissionLauncher()
        processCameraPermissionLogic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
        cityPickViewModel.currentlySelectedTransitAgency?.let { selected ->
            val initialData = PredictionViewModelInitialData(
                targetVehicleTypes = initialVehicleData.vehicleTypes,
                countryCharacters = resourceCountryCharacters.get(),
                selectedTransitAgency = selected,
                transitAgencyStop = null// todo
            )
            predictionViewModel.setInitialData(initialData)
        }
    }

    private fun observeNewFrameEvent() {
        predictionViewModel.newFrame.observe(viewLifecycleOwner, {
            fpsCounterManager.newFrameProcessed(System.currentTimeMillis())
        })
    }

    private fun observePredictedNumber() {
        predictionViewModel.predictedNumberLabel.observe(viewLifecycleOwner, { numberInfo ->
            val number = numberInfo.text
            if (number != null) {
                binding.predictedNumber.visibility = View.VISIBLE
                binding.predictedNumber.text = number
                binding.predictedNumber.announceForAccessibility(number)
            } else {
                binding.predictedNumber.visibility = View.GONE
            }
        })
    }

    private fun observeFpsCounter() {
        fpsCounterManager.observeFpsCounterUiChanges(viewLifecycleOwner, binding.fpsCounter)
    }

    private fun initRequestCameraPermissionLauncher() {
        requestCameraPermissionLauncher = registerForActivityResult(
            RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                processLocationPermissionLogic()
            } else {
                showExplanatoryWhyCameraPermissionIsRequired()
            }
        }
    }

    private fun processCameraPermissionLogic() {
        if (isCameraPermissionGranted()) {
            processLocationPermissionLogic()
        } else {
            requestCameraPermission()
        }
    }

    private fun isCameraPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        requestCameraPermissionLauncher.launch(CAMERA)
    }

    private fun startCameraAnalysis() {
        cameraAnalysis.startCameraAnalysis(textAnalyzer, binding.cameraPreview.surfaceProvider)
    }

    private fun showExplanatoryWhyCameraPermissionIsRequired() {
        showBinaryDialog(
            requireContext(),
            R.string.camera_permission_explanatory_title,
            R.string.camera_permission_explanatory_message,
            positiveAction = {
                processCameraPermissionLogic()
            },
            negativeAction = {
                goBackToPreviousScreen()
            }
        )
    }

    private fun goBackToPreviousScreen() {
        findNavController().navigateUp()
    }

    private fun initRequestLocationPermissionLauncher() {
        requestLocationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrElse(ACCESS_FINE_LOCATION, { false }) &&
                        permissions.getOrElse(ACCESS_COARSE_LOCATION, { false }) -> {
                    processLocationPermissionLogic()
                }
                else -> {
                    showExplanatoryWhyLocationPermissionIsRequired()
                }
            }
        }
    }

    private fun startTransitStationModule() {
        android.util.Log.d("test123", "startTransitStationModule()")
        // todo
    }

    private fun showExplanatoryWhyLocationPermissionIsRequired() {
        android.util.Log.d("test123", "showExplanatoryWhyLocationPermissionIsRequired()")
        // todo
    }

    private fun isLocationPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun processLocationPermissionLogic() {
        if (isLocationPermissionGranted()) {
            startCameraAnalysis()
            startTransitStationModule()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        requestLocationPermissionLauncher.launch(
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
        )
    }
}