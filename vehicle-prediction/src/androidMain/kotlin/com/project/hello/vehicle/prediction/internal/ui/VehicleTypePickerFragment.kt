package com.project.hello.vehicle.prediction.internal.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.project.hello.analytics.api.ScreenLogging
import com.project.hello.commons.actionbar.ActionBarUpIndicatorVisibility
import com.project.hello.commons.ui.showInformationDialog
import com.project.hello.commons.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelType
import com.project.hello.commons.viewmodel.externalViewModels
import com.project.hello.transit.agency.VehicleType
import com.project.hello.transit.agency.api.TransitAgencyPickViewModel
import com.project.hello.transit.agency.internal.datamodel.VehicleDataParcelable
import com.project.hello.vehicle.prediction.R
import com.project.hello.vehicle.prediction.databinding.VehicleTypePickerFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class VehicleTypePickerFragment : Fragment() {

    private lateinit var binding: VehicleTypePickerFragmentBinding

    @Inject
    lateinit var actionBarUpIndicatorVisibility: ActionBarUpIndicatorVisibility

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var transitAgencyPickViewModelProvider: ExternalViewModelProvider<TransitAgencyPickViewModel>

    private val transitAgencyViewModel by externalViewModels {
        transitAgencyPickViewModelProvider
    }

    @Inject
    lateinit var screenLogging: ScreenLogging

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VehicleTypePickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_prediction, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.go_to_settings_screen -> {
                val action = VehicleTypePickerFragmentDirections.goToSettingsScreen()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        announceScreenNameByScreenReader()
        setHasOptionsMenu(true)
        actionBarUpIndicatorVisibility.disableUpButtonIfPossible(activity)
        setupViews()
        logScreenEnteredEvent()
    }

    private fun announceScreenNameByScreenReader() {
        binding.root.announceForAccessibility(getString(R.string.vehicle_prediction_type_picker_fragment_label))
    }

    private fun setupViews() {
        setupHelpIconClicks()
        setupButtonClicks()
        updateButtonsVisibilityBasedOnSelectedTransitAgency()
    }

    private fun setupHelpIconClicks() {
        binding.helpIcon.setOnClickListener {
            showInformationDialog(
                it.context,
                R.string.vehicle_prediction_type_picker_help_title,
                R.string.vehicle_prediction_type_picker_help_message
            )
        }
    }

    private fun setupButtonClicks() {
        binding.tramTypesButton.setOnClickListener {
            val action = provideAction(listOf(VehicleType.TRAM))
            it.findNavController().navigate(action)
        }
        binding.busTypesButton.setOnClickListener {
            val action = provideAction(listOf(VehicleType.BUS))
            it.findNavController().navigate(action)
        }
    }

    private fun provideAction(vehicleTypes: List<VehicleType>): NavDirections {
        val vehicleData = VehicleDataParcelable(vehicleTypes)
        return VehicleTypePickerFragmentDirections.goToPredictionScreen(vehicleData)
    }

    private fun updateButtonsVisibilityBasedOnSelectedTransitAgency() {
        val transitAgency = transitAgencyViewModel.currentlySelectedTransitAgency
        if (transitAgency != null) {
            if (transitAgency.busLines.isEmpty()) {
                binding.busTypesButton.visibility = View.GONE
            }
            if (transitAgency.tramLines.isEmpty()) {
                binding.tramTypesButton.visibility = View.GONE
            }
        } else {
            binding.busTypesButton.visibility = View.GONE
            binding.tramTypesButton.visibility = View.GONE
        }
    }

    private fun logScreenEnteredEvent() {
        screenLogging.logScreen("vehicle-type-fragment")
    }
}