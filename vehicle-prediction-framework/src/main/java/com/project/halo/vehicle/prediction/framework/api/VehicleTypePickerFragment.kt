package com.project.halo.vehicle.prediction.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.halo.vehicle.prediction.framework.R
import com.project.halo.vehicle.prediction.framework.databinding.VehicleTypePickerFragmentBinding
import com.project.halo.vehicle.prediction.framework.internal.ui.showHelpDialog

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
}