package com.project.hallo.city.plan.framework.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hallo.city.plan.domain.model.CityPlan
import com.project.hallo.city.plan.framework.databinding.CityPickerFragmentBinding
import com.project.hallo.city.plan.framework.internal.ui.City
import com.project.hallo.city.plan.framework.internal.ui.CityPickerAdapter
import com.project.hallo.commons.framework.actionbar.ActionBarUpIndicatorVisibility
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

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    internal lateinit var internalCityPickViewModelProvider: ExternalViewModelProvider<InternalCityPickViewModel>

    private val internalCityPickViewModel by externalViewModels {
        internalCityPickViewModelProvider
    }

    @Inject
    lateinit var actionBarUpIndicatorVisibility: ActionBarUpIndicatorVisibility

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
        actionBarUpIndicatorVisibility.disableUpButtonIfPossible(activity)
        setUpViews()
        observeProgress()
        observeSupportedCities()
        observeCitySelection()
        observeCurrentlySelectedCityEvent()
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
            internalCityPickViewModel.selectCity(selectedCity)
        })
    }

    private fun observeProgress() {
        cityPickViewModel.processing.observe(viewLifecycleOwner, {
            setProgressVisibility(it)
        })
    }

    private fun observeCurrentlySelectedCityEvent() {
        cityPickViewModel.currentlySelectedCityEvent.observe(viewLifecycleOwner) {
            val content = it.content
            if (it.consumed.not() && content is CitySelection.NotSelected) {
                showErrorMessage(content.message)
            } else if (it.consumed.not() && content is CitySelection.Selected) {
                goBackToPreviousScreen()
            }
        }
    }

    private fun showErrorMessage(@StringRes message: Int) {
        // todo
    }

    private fun goBackToPreviousScreen() {
        findNavController().navigateUp()
    }

    private fun observeSupportedCities() {
        cityPickViewModel.supportedCities.observe(viewLifecycleOwner, {
            val content = it.content
            if (it.consumed.not() && content is SupportedCitiesStatus.Error) {
                fetchingSupportedCitiesFailed(content)
            } else if (content is SupportedCitiesStatus.Success) {
                fetchingSupportedCitiesSucceeded(content)
            }
        })
    }

    private fun fetchingSupportedCitiesFailed(status: SupportedCitiesStatus.Error) {
        showErrorMessage(status.message)
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
        val currentlySelected: CityPlan? = cityPickViewModel.currentlySelectedCity
        val cities = status.supportedCities.map {
            City(cityPlan = it, selected = currentlySelected?.city == it.city)
        }
        cityPickerAdapter.updateSupportedCities(cities)
    }

    companion object {
        const val RESULT_KEY = "CityPickerFragment_RESULT_KEY"
    }
}