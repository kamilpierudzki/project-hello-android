package com.project.hello.vehicle.prediction.framework.internal.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.project.hello.transit.agency.domain.VehicleType
import com.project.hello.transit.agency.framework.internal.datamodel.VehicleDataParcelable
import com.project.hello.commons.framework.actionbar.ActionBarUpIndicatorVisibility
import com.project.hello.commons.framework.ui.showInformationDialog
import com.project.hello.vehicle.prediction.framework.R
import com.project.hello.vehicle.prediction.framework.databinding.VehicleTypePickerFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class VehicleTypePickerFragment : Fragment() {

    private lateinit var binding: VehicleTypePickerFragmentBinding

    @Inject
    lateinit var actionBarUpIndicatorVisibility: ActionBarUpIndicatorVisibility

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
    }

    private fun announceScreenNameByScreenReader() {
        binding.root.announceForAccessibility(getString(R.string.vehicle_prediction_type_picker_fragment_label))
    }

    private fun setupViews() {
        setupHelpIconClicks()
        setupButtonClicks()
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
}