package com.project.hello.legal.framework.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.hello.commons.framework.actionbar.ActionBarUpIndicatorVisibility
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.commons.framework.viewmodel.externalViewModels
import com.project.hello.legal.framework.R
import com.project.hello.legal.framework.api.LatestAvailableLegalResult
import com.project.hello.legal.framework.api.LegalViewModel
import com.project.hello.legal.framework.databinding.LegalFragmentBinding
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
        announceScreenNameByScreenReader()
        disableUpButtonIfPossible()
        setupViews()
        observeAcceptationResult()
    }

    private fun announceScreenNameByScreenReader() {
        binding.root.announceForAccessibility(getString(R.string.legal_screen_label))
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
            when (event.consumeAndReturn()) {
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