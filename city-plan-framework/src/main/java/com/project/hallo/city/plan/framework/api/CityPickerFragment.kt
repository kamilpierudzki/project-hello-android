package com.project.hallo.city.plan.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hallo.city.plan.framework.databinding.CityPickerFragmentBinding
import com.project.hallo.city.plan.framework.internal.ui.CityPickerAdapter
import com.project.hallo.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelType
import com.project.hallo.commons.framework.viewmodel.externalViewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityPickerFragment : Fragment() {

    private lateinit var binding: CityPickerFragmentBinding

    @Inject
    internal lateinit var cityPickerAdapter: CityPickerAdapter

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
        binding = CityPickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeCitySelection()
        observeFetchingCityData()
        observeSupportedCities()
    }

    private fun setUpViews() {
        setupList()
    }

    private fun setupList() {
        binding.recyclerView.also {
            it.adapter = cityPickerAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeCitySelection() {
        cityPickerAdapter.citySelection.observe(viewLifecycleOwner, { selectedCity ->
            cityPickViewModel.selectCity(selectedCity)
        })
    }

    private fun observeFetchingCityData() {
        cityPickViewModel.fetchingCityStatus.observe(viewLifecycleOwner, { event ->
            when (event.getContentOrNull()) {
                FetchingCityStatus.Loading,
                FetchingCityStatus.Success -> navigateToSplashScreen()
                FetchingCityStatus.Error -> showErrorMessage()
            }
        })
    }

    private fun navigateToSplashScreen() {
        val navController = findNavController()
        val startDestination = navController.graph.startDestination
        navController.navigate(startDestination)
    }

    private fun showErrorMessage() {
        // todo show error message
    }


    private fun observeSupportedCities() {
        cityPickViewModel.supportedCities.observe(viewLifecycleOwner, { status ->
            when (status) {
                is FetchingSupportedCitiesStatus.Error -> fetchingSupportedCitiesFailed(status)
                FetchingSupportedCitiesStatus.Loading -> setupUiForLoadingStatus()
                is FetchingSupportedCitiesStatus.Success -> fetchingSupportedCitiesSucceeded(status)
            }
        })
    }

    private fun fetchingSupportedCitiesFailed(status: FetchingSupportedCitiesStatus.Error) {
        showErrorMessage(status.message)
        binding.root.setOnRefreshListener {
            cityPickViewModel.forceFetchSupportedCities()
        }
    }

    private fun showErrorMessage(message: String) {
        // todo show message
    }

    private fun setupUiForLoadingStatus() {
        binding.apply {
            recyclerView.visibility = View.GONE
            progressLabel.visibility = View.VISIBLE
            progress.visibility = View.VISIBLE
        }
    }

    private fun fetchingSupportedCitiesSucceeded(status: FetchingSupportedCitiesStatus.Success) {
        cityPickerAdapter.updateData(status.supportedCities)
        binding.root.setOnRefreshListener(null)
    }
}