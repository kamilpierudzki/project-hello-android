package com.project.hallo.vehicle.prediction.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.project.hallo.city.plan.domain.VehicleData
import com.project.hallo.city.plan.domain.VehicleType
import com.project.hallo.vehicle.prediction.framework.R
import com.project.hallo.vehicle.prediction.framework.databinding.VehicleTypePickerFragmentBinding
import com.project.hallo.vehicle.prediction.framework.internal.ui.showHelpDialog

class VehicleTypePickerFragment : Fragment() {

    private var _binding: VehicleTypePickerFragmentBinding? = null
    private val binding get() = _binding!!

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
            showHelpDialog(
                it.context,
                R.string.vehicle_type_picker_help_title,
                R.string.vehicle_type_picker_help_message
            )
        }
    }

    private fun setupButtonClicks() {
        binding.allTypesButton.setOnClickListener {
            val vehicleData = VehicleData(listOf(VehicleType.TRAM, VehicleType.BUS))
            val action = VehicleTypePickerFragmentDirections.goToPredictionScreen(vehicleData)
            it.findNavController().navigate(action)
        }
        binding.tramTypesButton.setOnClickListener {
            val vehicleData = VehicleData(listOf(VehicleType.TRAM))
            val action = VehicleTypePickerFragmentDirections.goToPredictionScreen(vehicleData)
            it.findNavController().navigate(action)
        }
        binding.busTypesButton.setOnClickListener {
            val vehicleData = VehicleData(listOf(VehicleType.BUS))
            val action = VehicleTypePickerFragmentDirections.goToPredictionScreen(vehicleData)
            it.findNavController().navigate(action)
        }
    }
}