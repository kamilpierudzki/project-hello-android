package com.project.hallo.legal.framework.internal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.hallo.legal.framework.databinding.LegalFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class LegalFragment : Fragment() {

    private lateinit var binding: LegalFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LegalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}