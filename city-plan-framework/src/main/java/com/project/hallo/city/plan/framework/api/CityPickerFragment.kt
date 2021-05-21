package com.project.hallo.city.plan.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hallo.city.plan.framework.databinding.CityPickerFragmentBinding
import com.project.hallo.city.plan.framework.internal.ui.City
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
        observeProgress()
        observeSupportedCities()
        observeCitySelection()
        cityPickViewModel.forceFetchSupportedCities()
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

    private fun observeProgress() {
        cityPickViewModel.processing.observe(viewLifecycleOwner, {
            setProgressVisibility(it)
        })
    }

    private fun observeSupportedCities() {
        cityPickViewModel.supportedCities.observe(viewLifecycleOwner, {
            when (val status = it.getContentOrNull()) {
                is SupportedCitiesStatus.Error -> fetchingSupportedCitiesFailed(status)
                is SupportedCitiesStatus.Success -> fetchingSupportedCitiesSucceeded(status)
            }
        })
    }

    private fun fetchingSupportedCitiesFailed(status: SupportedCitiesStatus.Error) {
        showErrorMessage(status.message)
        binding.root.setOnRefreshListener {
            cityPickViewModel.forceFetchSupportedCities()
        }
    }

    private fun showErrorMessage(message: String) {
        // todo
    }

    private fun setProgressVisibility(visible: Boolean) {
        binding.progressLabel.visibility = if (visible) View.VISIBLE else View.GONE
        binding.progress.visibility = if (visible) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (visible) View.GONE else View.VISIBLE
    }

    private fun fetchingSupportedCitiesSucceeded(status: SupportedCitiesStatus.Success) {
        val cities = status.supportedCities.map {
            City(it.cityPlan, it.currentlySelected)
        }
        cityPickerAdapter.updateData(cities)
        binding.root.setOnRefreshListener(null)
    }
}