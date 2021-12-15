package com.project.hello.legal.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.hello.commons.actionbar.ActionBarUpIndicatorVisibility
import com.project.hello.commons.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelProvider
import com.project.hello.commons.viewmodel.ViewModelType
import com.project.hello.commons.viewmodel.externalViewModels
import com.project.hello.legal.R
import com.project.hello.legal.api.LatestAvailableLegalResult
import com.project.hello.legal.api.LegalViewModel
import com.project.hello.legal.databinding.LegalFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class LegalFragment : Fragment() {

    private lateinit var binding: LegalFragmentBinding

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
        actionBarUpIndicatorVisibility.disableUpButtonIfPossible(activity)
        announceScreenNameByScreenReader()
        setupViews()
        observeAcceptationResult()
    }

    private fun announceScreenNameByScreenReader() {
        binding.root.announceForAccessibility(getString(R.string.legal_screen_label))
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
            when (event.consumeAndReturnOrNull()) {
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