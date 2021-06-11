package com.project.hallo.vehicle.prediction.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.project.hallo.city.plan.domain.VehicleData
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.city.plan.framework.internal.datamodel.CityPlanParcelable
import com.project.hallo.city.plan.framework.internal.datamodel.VehicleDataParcelable
import com.project.hallo.vehicle.prediction.framework.R
import com.project.hallo.vehicle.prediction.framework.databinding.VehicleTypePickerFragmentBinding
import com.project.hallo.vehicle.prediction.framework.internal.ui.showNeutralDialog

class VehicleTypePickerFragment : Fragment() {

    private var _binding: VehicleTypePickerFragmentBinding? = null
    private val binding get() = _binding!!
    private val safeArgs: VehicleTypePickerFragmentArgs by navArgs()
    private val selectedCityParcelable: CityPlanParcelable get() = safeArgs.selectedCityParcelable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VehicleTypePickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupHelpIconClicks()
        setupButtonClicks()
    }

    private fun setupHelpIconClicks() {
        binding.helpIcon.setOnClickListener {
            showNeutralDialog(
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
        return VehicleTypePickerFragmentDirections.goToPredictionScreen(
            vehicleData,
            selectedCityParcelable
        )
    }
}