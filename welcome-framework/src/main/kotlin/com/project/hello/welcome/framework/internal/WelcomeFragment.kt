package com.project.hello.welcome.framework.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.project.hello.commons.framework.actionbar.ActionBarUpIndicatorVisibility
import com.project.hello.commons.framework.ui.showInformationDialog
import com.project.hello.commons.framework.viewmodel.ExternalViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelProvider
import com.project.hello.commons.framework.viewmodel.ViewModelType
import com.project.hello.commons.framework.viewmodel.externalViewModels
import com.project.hello.welcome.framework.R
import com.project.hello.welcome.framework.api.WelcomeViewModel
import com.project.hello.welcome.framework.databinding.WelcomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class WelcomeFragment : Fragment() {

    private lateinit var binding: WelcomeFragmentBinding

    @Inject
    lateinit var actionBarUpIndicatorVisibility: ActionBarUpIndicatorVisibility

    @Inject
    @ViewModelProvider(ViewModelType.ACTIVITY)
    lateinit var welcomeViewModelProvider: ExternalViewModelProvider<WelcomeViewModel>

    private val welcomeViewModel by externalViewModels {
        welcomeViewModelProvider
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarUpIndicatorVisibility.disableUpButtonIfPossible(activity)
        setupViews()
        observeIfFirstLaunch()
    }

    private fun setupViews() {
        setupNextScreenButtonClicks()
        setupIntroductionButtonClicks()
    }

    private fun setupNextScreenButtonClicks() {
        binding.nextScreenButton.setOnClickListener {
            showInformationDialog(
                it.context,
                R.string.information,
                R.string.welcome_screen_dialog_message
            ) {
                welcomeViewModel.markFirstLaunchAccomplished()
            }
        }
    }

    private fun setupIntroductionButtonClicks() {
        binding.introductionButton.setOnClickListener {
            goToIntroductionScreen()
        }
    }

    private fun goToIntroductionScreen() {
        val action = WelcomeFragmentDirections.goToIntroductionScreen()
        findNavController().navigate(action)
    }

    private fun goBackToPreviousScreen() {
        findNavController().navigateUp()
    }

    private fun observeIfFirstLaunch() {
        welcomeViewModel.isFirstLaunch.observe(viewLifecycleOwner, { event ->
            if (!event.content) {
                goBackToPreviousScreen()
            }
        })
    }
}