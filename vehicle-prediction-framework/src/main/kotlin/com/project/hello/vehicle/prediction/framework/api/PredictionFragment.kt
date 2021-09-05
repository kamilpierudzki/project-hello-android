package com.project.hello.vehicle.prediction.framework.api

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.commons.framework.ui.showBinaryDialog
import com.project.hello.commons.framework.ui.showInformationDialog
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.commons.framework.viewmodel.externalViewModels
import com.project.hello.country.api.ResourceCountryCharacters
import com.project.hello.vehicle.prediction.framework.internal.FpsCounterWrapper
import com.project.hello.vehicle.prediction.framework.internal.PredictionViewModel
import com.project.hello.vehicle.prediction.framework.internal.PredictionViewModelInitialData
import com.project.hello.vehicle.prediction.framework.internal.camera.CameraAnalysis
import com.project.hello.vehicle.prediction.framework.internal.textrecognition.DisposableImageAnalyzer
import com.project.hello.vehicle.prediction.framework.internal.ui.PredictedLinesAdapter
import com.project.hello.city.plan.framework.api.CityPickViewModel
import com.project.hello.city.plan.framework.internal.datamodel.VehicleDataParcelable
import com.project.hello.vehicle.prediction.framework.R
import com.project.hello.vehicle.prediction.framework.databinding.PredictionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.DONUT)
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
    private lateinit var predictedLinesAdapter: PredictedLinesAdapter
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
        setupViews()
        passInitialInfoToViewModel()
        observePredictedLines()
        observeRecognisedTexts()
        observeFpsCounter()
        observeScreenContentDescription()
        initRequestCameraPermissionLauncher()
        processPermissionLogic()
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
            showInformationDialog(
                it.context,
                R.string.help_dialog_title,
                R.string.help_dialog_message
            )
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