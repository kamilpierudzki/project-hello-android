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
        provideTemporaryDataForList()
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
                FetchingCityStatus.InProgress,
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

    private fun provideTemporaryDataForList() {
        cityPickerAdapter.updateData(listOf("Poznań", "Kraków", "Warszawa"))
    }
}