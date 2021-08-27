package com.project.hallo.legal.framework.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.hallo.commons.framework.actionbar.ActionBarUpIndicatorVisibility
import com.project.hallo.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelProvider
import com.project.hallo.commons.framework.viewmodel.ViewModelType
import com.project.hallo.commons.framework.viewmodel.externalViewModels
import com.project.hallo.legal.framework.api.LatestAvailableLegalResult
import com.project.hallo.legal.framework.api.LegalViewModel
import com.project.hallo.legal.framework.databinding.LegalFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class LegalFragment : Fragment() {

    private lateinit var binding: LegalFragmentBinding
    private val safeArgs: LegalFragmentArgs by navArgs()
    private val backButtonDisabled: Boolean get() = safeArgs.backButtonDisabled

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var legalViewModelProvider: ExternalViewModelProvider<LegalViewModel>

    private val legalViewModel by externalViewModels {
        legalViewModelProvider
    }

    @Inject
    lateinit var actionBarUpIndicatorVisibility: ActionBarUpIndicatorVisibility

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LegalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableUpButtonIfPossible()
        setupViews()
        observeAcceptationResult()
    }

    private fun disableUpButtonIfPossible() {
        if (backButtonDisabled) {
            actionBarUpIndicatorVisibility.disableUpButtonIfPossible(activity)
        }
    }

    private fun setupViews() {
        setupMessage()
        setupButtonAction()
    }

    private fun setupMessage() {
        binding.legalMessage.text = legalViewModel.latestAvailableLegal.value?.message
    }

    private fun setupButtonAction() {
        binding.confirmationButton.setOnClickListener {
            setProgressVisibility(true)
            legalViewModel.onLatestAvailableLegalAccepted()
        }
    }

    private fun setProgressVisibility(visible: Boolean) {
        binding.progressLabel.visibility = if (visible) View.VISIBLE else View.GONE
        binding.progress.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun observeAcceptationResult() {
        legalViewModel.latestAvailableLegalSavedResult.observe(viewLifecycleOwner, { event ->
            when (event.getContentOrNull()) {
                is LatestAvailableLegalResult.Error -> {
                    setProgressVisibility(false)
                }
                LatestAvailableLegalResult.Success -> {
                    goBackToPreviousScreen()
                }
            }
        })
    }

    private fun goBackToPreviousScreen() {
        findNavController().navigateUp()
    }
}