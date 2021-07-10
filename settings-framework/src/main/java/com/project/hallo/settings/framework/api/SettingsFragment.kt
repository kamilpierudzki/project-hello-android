package com.project.hallo.settings.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelType
import com.project.hallo.commons.framework.viewmodel.externalViewModels
import com.project.hello.city.plan.framework.api.CityPickViewModel
import com.projekt.hallo.settings.framework.databinding.SettingsFragmentBinding
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
        setupViews()
        setupBinding()
    }

    private fun setupViews() {
        setupCitySelection()
    }

    private fun setupCitySelection() {
        binding.pickCityRow.root.setOnClickListener {
            goToCityPickerScreen(it)
        }
    }

    private fun goToCityPickerScreen(view: View) {
        val action = SettingsFragmentDirections.goToCityPickerScreen(backButtonDisabled = false)
        view.findNavController().navigate(action)
    }

    private fun setupBinding() {
        val currentlySelectedCity: CityPlan? = cityPickViewModel.currentlySelectedCity
        if (currentlySelectedCity != null) {
            binding.pickCityRow.selected.text = currentlySelectedCity.city
        }
    }
}