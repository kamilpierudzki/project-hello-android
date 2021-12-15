package com.project.hello.splash.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.hello.commons.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelType
import com.project.hello.commons.viewmodel.externalViewModels
import com.project.hello.legal.api.LegalViewModel
import com.project.hello.splash.databinding.SplashFragmentBinding
import com.project.hello.transit.agency.api.TransitAgencyPickViewModel
import com.project.hello.transit.agency.api.TransitAgencySelection
import com.project.hello.welcome.api.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class SplashFragment : Fragment() {

    private lateinit var binding: SplashFragmentBinding

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var transitAgencyPickViewModelProvider: ExternalViewModelProvider<TransitAgencyPickViewModel>

    private val cityPickViewModel by externalViewModels {
        transitAgencyPickViewModelProvider
    }

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var legalViewModelProvider: ExternalViewModelProvider<LegalViewModel>

    private val legalViewModel by externalViewModels {
        legalViewModelProvider
    }

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var welcomeViewModelProvider: ExternalViewModelProvider<WelcomeViewModel>

    private val welcomeViewModel by externalViewModels {
        welcomeViewModelProvider
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
            when (event.consumeAndReturnOrNull()) {
                false -> goToLegalScreen()
                else -> when (event.content) {
                    true -> observeIfFirstLaunch()
                    false -> finishActivity()
                }
            }
        })
    }

    private fun observeIfFirstLaunch() {
        welcomeViewModel.isFirstLaunch.observe(viewLifecycleOwner, { event ->
            when (event.consumeAndReturnOrNull()) {
                true -> goToWelcomeScreen()
                else -> when (event.content) {
                    true -> finishActivity()
                    false -> observeCurrentlySelectedCity()
                }
            }
        })
    }

    private fun observeCurrentlySelectedCity() {
        cityPickViewModel.currentlySelectedTransitAgencyEvent.observe(viewLifecycleOwner, { event ->
            when (event.consumeAndReturnOrNull()) {
                is TransitAgencySelection.NotSelected -> goToCityPickerScreen()
                is TransitAgencySelection.Selected -> goToVehicleTypePickerScreen()
                else -> finishActivity()
            }
        })
    }

    private fun goToCityPickerScreen() {
        val action =
            SplashFragmentDirections.goToTransitAgencyPickerScreen(true)
        findNavController().navigate(action)
    }

    private fun goToVehicleTypePickerScreen() {
        val action = SplashFragmentDirections.goToVehicleTypePickerScreen()
        findNavController().navigate(action)
    }

    private fun goToLegalScreen() {
        val action = SplashFragmentDirections.goToLegalScreen()
        findNavController().navigate(action)
    }

    private fun goToWelcomeScreen() {
        val action = SplashFragmentDirections.goToWelcomeScreen()
        findNavController().navigate(action)
    }

    private fun finishActivity() {
        requireActivity().finish()
    }
}