package com.project.hallo.vehicle.prediction.framework.api

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.framework.internal.datamodel.VehicleDataParcelable
import com.project.hallo.commons.framework.actionbar.ActionBarUpIndicatorVisibility
import com.project.hallo.vehicle.prediction.framework.R
import com.project.hallo.vehicle.prediction.framework.databinding.VehicleTypePickerFragmentBinding
import com.project.hallo.vehicle.prediction.framework.internal.ui.showInformationDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.HONEYCOMB)
@AndroidEntryPoint
class VehicleTypePickerFragment : Fragment() {

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
        setHasOptionsMenu(true)
        actionBarUpIndicatorVisibility.disableUpButtonIfPossible(activity)
        setupViews()
    }

    private fun setupViews() {
        setupHelpIconClicks()
        setupButtonClicks()
    }

    private fun setupHelpIconClicks() {
        binding.helpIcon.setOnClickListener {
            showInformationDialog(
                it.context,
                R.string.vehicle_type_picker_help_title,
                R.string.vehicle_type_picker_help_message
            )
        }
    }

    private fun setupButtonClicks() {
        binding.allTypesButton.setOnClickListener {
            val action = provideAction(listOf(VehicleType.TRAM, VehicleType.BUS))
            it.findNavController().navigate(action)
        }
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