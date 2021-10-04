package com.project.hello.city.plan.framework.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hello.city.plan.domain.model.TransitAgency
import com.project.hello.city.plan.framework.R
import com.project.hello.city.plan.framework.api.InternalTransitAgencyPickViewModel
import com.project.hello.city.plan.framework.api.SupportedTransitAgenciesStatus
import com.project.hello.city.plan.framework.api.TransitAgencyPickViewModel
import com.project.hello.city.plan.framework.api.TransitAgencySelection
import com.project.hello.city.plan.framework.databinding.TransitAgencyPickerFragmentBinding
import com.project.hello.city.plan.framework.internal.ui.TransitAgencyPickerAdapter
import com.project.hello.city.plan.framework.internal.ui.TransitAgencyPickerRow
import com.project.hello.commons.framework.actionbar.ActionBarUpIndicatorVisibility
import com.project.hello.commons.framework.ui.showInformationDialog
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.commons.framework.viewmodel.externalViewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class TransitAgencyPickerFragment : Fragment() {

    private lateinit var binding: TransitAgencyPickerFragmentBinding
    private val safeArgs: TransitAgencyPickerFragmentArgs by navArgs()
    private val backButtonDisabled: Boolean get() = safeArgs.backButtonDisabled
    private var isCitySelectionProcessing = false

    @Inject
    internal lateinit var transitAgencyPickerAdapter: TransitAgencyPickerAdapter

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var transitAgencyPickViewModelProvider: ExternalViewModelProvider<TransitAgencyPickViewModel>

    private val cityPickViewModel by externalViewModels {
        transitAgencyPickViewModelProvider
    }

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    internal lateinit var internalTransitAgencyPickViewModelProvider: ExternalViewModelProvider<InternalTransitAgencyPickViewModel>

    private val internalCityPickViewModel by externalViewModels {
        internalTransitAgencyPickViewModelProvider
    }

    @Inject
    lateinit var actionBarUpIndicatorVisibility: ActionBarUpIndicatorVisibility

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TransitAgencyPickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        announceScreenNameByScreenReader()
        disableUpButtonIfPossible()
        setUpViews()
        observeProgress()
        observeSupportedCities()
        observeCitySelection()
        observeInfoSelection()
        observeCurrentlySelectedCityEvent()
    }

    private fun announceScreenNameByScreenReader() {
        binding.root.announceForAccessibility(getString(R.string.transit_agency_picker_label))
    }

    private fun disableUpButtonIfPossible() {
        if (backButtonDisabled) {
            actionBarUpIndicatorVisibility.disableUpButtonIfPossible(activity)
        }
    }

    private fun setUpViews() {
        setupList()
    }

    private fun setupList() {
        binding.recyclerView.also {
            it.adapter = transitAgencyPickerAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeCitySelection() {
        transitAgencyPickerAdapter.transitAgencySelection.observe(
            viewLifecycleOwner,
            { selectedCity ->
                isCitySelectionProcessing = true
                internalCityPickViewModel.selectTransitAgency(selectedCity)
            })
    }

    private fun observeInfoSelection() {
        transitAgencyPickerAdapter.infoSelection.observe(viewLifecycleOwner, {
            showInfoDialog()
        })
    }

    private fun showInfoDialog() {
        showInformationDialog(
            requireContext(),
            R.string.no_transit_agency_dialog_title,
            R.string.no_transit_agency_dialog_message
        )
    }

    private fun observeProgress() {
        cityPickViewModel.processing.observe(viewLifecycleOwner, {
            setProgressVisibility(it)
        })
    }

    private fun observeCurrentlySelectedCityEvent() {
        cityPickViewModel.currentlySelectedTransitAgencyEvent.observe(viewLifecycleOwner) {
            if (isCitySelectionProcessing) {
                isCitySelectionProcessing = false
                val content = it.content
                if (!it.consumed && content is TransitAgencySelection.NotSelected) {
                    currentlySelectedCityFailed(content)
                } else if (!it.consumed && content is TransitAgencySelection.Selected) {
                    goBackToPreviousScreen()
                }
            }
        }
    }

    private fun currentlySelectedCityFailed(status: TransitAgencySelection.NotSelected) {
        showErrorMessage(status.message)
    }

    private fun goBackToPreviousScreen() {
        findNavController().navigateUp()
    }

    private fun observeSupportedCities() {
        cityPickViewModel.supportedTransitAgencies.observe(viewLifecycleOwner, {
            val content = it.content
            if (it.consumed.not() && content is SupportedTransitAgenciesStatus.Error) {
                fetchingSupportedCitiesFailed(content)
            } else if (content is SupportedTransitAgenciesStatus.Success) {
                fetchingSupportedCitiesSucceeded(content)
            }
        })
    }

    private fun fetchingSupportedCitiesFailed(status: SupportedTransitAgenciesStatus.Error) {
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

    private fun fetchingSupportedCitiesSucceeded(status: SupportedTransitAgenciesStatus.Success) {
        val currentlySelected: TransitAgency? = cityPickViewModel.currentlySelectedCity
        val cities = status.supportedTransitAgencies.map {
            TransitAgencyPickerRow.TransitAgencyRow(
                transitAgency = it,
                selected = currentlySelected?.transitAgency == it.transitAgency
            )
        }
        transitAgencyPickerAdapter.updateSupportedCities(cities)
    }
}