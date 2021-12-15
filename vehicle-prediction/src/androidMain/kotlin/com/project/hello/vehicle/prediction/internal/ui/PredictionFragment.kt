package com.project.hello.vehicle.prediction.internal.ui

import android.Manifest.permission.*
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.hello.commons.ui.showBinaryDialog
import com.project.hello.commons.ui.showInformationDialog
import com.project.hello.commons.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelType
import com.project.hello.commons.viewmodel.externalViewModels
import com.project.hello.transit.agency.api.TransitAgencyPickViewModel
import com.project.hello.transit.agency.internal.datamodel.VehicleDataParcelable
import com.project.hello.vehicle.prediction.databinding.PredictionFragmentBinding
import com.project.hello.vehicle.prediction.R
import com.project.hello.vehicle.prediction.internal.camera.CameraAnalysis
import com.project.hello.vehicle.prediction.internal.characters.ResourceCountryCharacters
import com.project.hello.vehicle.prediction.internal.station.LocationSettingsSatisfaction
import com.project.hello.vehicle.prediction.internal.textrecognition.DisposableImageAnalyzer
import com.project.hello.vehicle.prediction.internal.viewmodel.PredictionViewModel
import com.project.hello.vehicle.prediction.internal.viewmodel.PredictionViewModelInitialData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val REQUEST_CHECK_SETTINGS = 1806

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

    private val transitAgencyPickViewModel by externalViewModels {
        transitAgencyPickViewModelProvider
    }

    @Inject
    lateinit var cityLinesInfo: CityLinesInfo

    @Inject
    lateinit var cameraRatingUi: CameraRatingUi

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
        observeCityLinesInfoEvent()
        observeCityLinesInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textAnalyzer.dispose()
    }

    private fun announceScreenNameByScreenReader() {
        binding.root.announceForAccessibility(getString(R.string.vehicle_prediction_fragment_label))
    }

    private fun setupViews() {
        setupHelpIconClicks()
        setupCameraRatingClicks()
    }

    private fun setupHelpIconClicks() {
        binding.helpIcon.setOnClickListener {
            showInformationDialog(
                it.context,
                R.string.vehicle_prediction_help_dialog_title,
                R.string.vehicle_prediction_help_dialog_message
            )
        }
    }

    private fun setupCameraRatingClicks() {
        binding.cameraRating.setOnClickListener {
            showInformationDialog(
                it.context,
                R.string.vehicle_prediction_camera_rating_dialog_title,
                R.string.vehicle_prediction_camera_rating_dialog_message
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
        transitAgencyPickViewModel.currentlySelectedTransitAgency?.let { selected ->
            val initialData = PredictionViewModelInitialData(
                targetVehicleTypes = initialVehicleData.vehicleTypes,
                countryCharacters = resourceCountryCharacters.get(),
                selectedTransitAgency = selected,
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
                processCameraPermissionLogic()
            } else {
                showExplanatoryWhyCameraPermissionIsRequired()
            }
        }
    }

    private fun processCameraPermissionLogic() {
        if (isCameraPermissionGranted()) {
            cameraRatingUi.updateCameraHardwareRatingUi(binding.cameraRating)
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
            R.string.vehicle_prediction_camera_permission_explanatory_title,
            R.string.vehicle_prediction_camera_permission_explanatory_message,
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
        predictionViewModel.startTransitStationModule()
    }

    private fun showExplanatoryWhyLocationPermissionIsRequired() {
        showBinaryDialog(
            requireContext(),
            R.string.vehicle_prediction_location_permission_explanatory_title,
            R.string.vehicle_prediction_location_permission_explanatory_message,
            positiveAction = {
                processLocationPermissionLogic()
            },
            negativeAction = {
                goBackToPreviousScreen()
            }
        )
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
            observeLocationSettingsSatisfaction()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        requestLocationPermissionLauncher.launch(
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
        )
    }

    private fun observeLocationSettingsSatisfaction() {
        predictionViewModel.locationSettingsSatisfactionEvent.observe(viewLifecycleOwner) { event ->
            when (val result = event.consumeAndReturnOrNull()) {
                is LocationSettingsSatisfaction.NotSatisfied -> {
                    try {
                        val exception = result.exception
                        exception.startResolutionForResult(
                            requireActivity(),
                            REQUEST_CHECK_SETTINGS
                        )
                        goBackToPreviousScreen()
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }
    }

    private fun observeCityLinesInfoEvent() {
        predictionViewModel.cityLinesEvent.observe(viewLifecycleOwner, {
            cityLinesInfo.updateCityLinesInfo(it)
        })
    }

    private fun observeCityLinesInfo() {
        cityLinesInfo.observeCityLinesInfoUiChanges(viewLifecycleOwner, binding.cityLinesCount)
    }
}