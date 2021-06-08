package com.project.hallo.splash.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.framework.api.CityPickViewModel
import com.project.hallo.city.plan.framework.api.CityPickerFragment
import com.project.hallo.city.plan.framework.api.CitySelection
import com.project.hallo.city.plan.framework.internal.datamodel.CityPlanParcelable
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
        observeCityPickerScreenResult()
    }

    private fun observeCurrentlySelectedCity() {
        cityPickViewModel.currentlySelectedCity.observe(viewLifecycleOwner, { event ->
            when (val selection = event.getContentOrNull()) {
                is CitySelection.NotSelected -> goToCityPickerScreen()
                is CitySelection.Selected -> handleSelectedCity(selection.cityPlan)
            }
        })
    }

    private fun goToCityPickerScreen() {
        findNavController().navigate(R.id.city_plan_nav_graph)
    }

    private fun handleSelectedCity(cityPlan: CityPlan) {
        val cityPlanParcelable = CityPlanParcelable.fromCityPlan(cityPlan)
        goToVehicleTypePickerScreen(cityPlanParcelable)
    }

    private fun goToVehicleTypePickerScreen(cityPlanParcelable: CityPlanParcelable) {
        val action = SplashFragmentDirections.goToVehicleTypePickerScreen(cityPlanParcelable)
        findNavController().navigate(action)
    }

    private fun observeCityPickerScreenResult() {
        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        val liveData = savedStateHandle?.getLiveData<CityPlanParcelable>(CityPickerFragment.RESULT_KEY)
        liveData?.observe(viewLifecycleOwner) { result: CityPlanParcelable ->
            savedStateHandle.remove<CityPlanParcelable>(CityPickerFragment.RESULT_KEY)
            goToVehicleTypePickerScreen(result)
        }
    }
}