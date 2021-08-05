package com.project.hallo.legal.framework.internal

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LegalViewModel @Inject constructor(): ViewModel() {

    init {

    }

    override fun onCleared() {

    }
}