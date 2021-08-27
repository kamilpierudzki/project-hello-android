package com.project.hallo.splash.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.hallo.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelType
import com.project.hallo.commons.framework.viewmodel.externalViewModels
import com.project.hallo.legal.framework.api.LegalViewModel
import com.project.hallo.splash.framework.databinding.SplashFragmentBinding
import com.project.hello.city.plan.framework.api.CityPickViewModel
import com.project.hello.city.plan.framework.api.CitySelection
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

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var legalViewModelProvider: ExternalViewModelProvider<LegalViewModel>

    private val legalViewModel by externalViewModels {
        legalViewModelProvider
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
        observeIfLatestAvailableLegalIsAccepted()
    }

    private fun observeIfLatestAvailableLegalIsAccepted() {
        legalViewModel.isLatestAvailableLegalAccepted.observe(viewLifecycleOwner, { event ->
            when (event.getContentOrNull()) {
                true -> observeCurrentlySelectedCity()
                false -> goToLegalScreen()
            }
        })
    }

    private fun observeCurrentlySelectedCity() {
        cityPickViewModel.currentlySelectedCityEvent.observe(viewLifecycleOwner, { event ->
            when (event.getContentOrNull()) {
                is CitySelection.NotSelected -> goToCityPickerScreen()
                is CitySelection.Selected -> goToVehicleTypePickerScreen()
            }
        })
    }

    private fun goToCityPickerScreen() {
        val action = SplashFragmentDirections.goToCityPickerScreen(backButtonDisabled = true)
        findNavController().navigate(action)
    }

    private fun goToVehicleTypePickerScreen() {
        val action = SplashFragmentDirections.goToVehicleTypePickerScreen()
        findNavController().navigate(action)
    }

    private fun goToLegalScreen() {
        val action = SplashFragmentDirections.goToLegalScreen(backButtonDisabled = true)
        findNavController().navigate(action)
    }
}