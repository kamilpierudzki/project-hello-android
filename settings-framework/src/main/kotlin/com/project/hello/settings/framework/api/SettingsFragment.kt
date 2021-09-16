package com.project.hello.settings.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.project.hello.city.plan.domain.model.CityPlan
import com.project.hello.city.plan.framework.api.CityPickViewModel
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.commons.framework.viewmodel.externalViewModels
import com.project.hello.settings.framework.R
import com.project.hello.settings.framework.databinding.SettingsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: SettingsFragmentBinding

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
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        announceScreenNameByScreenReader()
        setupViews()
        setupBinding()
    }

    private fun announceScreenNameByScreenReader() {
        binding.root.announceForAccessibility(getString(R.string.settings_screen_label))
    }

    private fun setupViews() {
        setupCityPickerClicks()
        setupIntroductionRowClicks()
    }

    private fun setupCityPickerClicks() {
        binding.pickCityRow.root.setOnClickListener {
            goToCityPickerScreen(it)
        }
    }

    private fun goToCityPickerScreen(view: View) {
        val action = SettingsFragmentDirections.goToCityPickerScreen(backButtonDisabled = false)
        view.findNavController().navigate(action)
    }

    private fun setupIntroductionRowClicks() {
        binding.introductionScreen.setOnClickListener {
            goToIntroductionScreen(it)
        }
    }

    private fun goToIntroductionScreen(view: View) {
        val action = SettingsFragmentDirections.goToIntroductionScreen()
        view.findNavController().navigate(action)
    }

    private fun setupBinding() {
        val currentlySelectedCity: CityPlan? = cityPickViewModel.currentlySelectedCity
        if (currentlySelectedCity != null) {
            binding.pickCityRow.selected.text = currentlySelectedCity.city
            updateContentDescriptionForRow(currentlySelectedCity)
        }
    }

    private fun updateContentDescriptionForRow(currentlySelectedCity: CityPlan) {
        binding.pickCityRow.root.contentDescription = """
            ${getString(R.string.settings_select_city)}. 
             ${getString(R.string.settings_select_city_currently_selected)}:
             ${currentlySelectedCity.city}"
        """.trimIndent()
    }
}