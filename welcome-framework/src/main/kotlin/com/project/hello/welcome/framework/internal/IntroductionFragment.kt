package com.project.hello.welcome.framework.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.project.hello.welcome.framework.databinding.IntroductionFragmentBinding

@AndroidEntryPoint
internal class IntroductionFragment : Fragment() {

    private lateinit var binding: IntroductionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = IntroductionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}