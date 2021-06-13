package com.project.hallo.splash.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.hallo.city.plan.framework.api.CityPickViewModel
import com.project.hallo.city.plan.framework.api.CitySelection
import com.project.hallo.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelType
import com.project.hallo.commons.framework.viewmodel.externalViewModels
import com.project.hallo.splash.framework.R
import com.project.hallo.splash.framework.databinding.SplashFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: SplashFragmentBinding

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var cityPickViewModelProvider: ExternalViewModelProvider<CityPickViewModel>

    private val cityPickViewModel by externalViewModels {
        cityPickViewModelProvider
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCurrentlySelectedCity()
    }

    private fun observeCurrentlySelectedCity() {
        cityPickViewModel.currentlySelectedCityEvent.observe(viewLifecycleOwner, { event ->
            when (event.getContentOrNull()) {
                is CitySelection.NotSelected -> goToCityPickerScreen()
                is CitySelection.Selected -> handleSelectedCity()
            }
        })
    }

    private fun goToCityPickerScreen() {
        findNavController().navigate(R.id.city_plan_nav_graph)
    }

    private fun handleSelectedCity() {
        goToVehicleTypePickerScreen()
    }

    private fun goToVehicleTypePickerScreen() {
        val action = SplashFragmentDirections.goToVehicleTypePickerScreen()
        findNavController().navigate(action)
    }
}