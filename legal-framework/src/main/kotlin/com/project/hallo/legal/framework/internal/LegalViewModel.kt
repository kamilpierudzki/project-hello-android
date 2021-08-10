package com.project.hallo.legal.framework.internal

import androidx.lifecycle.ViewModel
import com.project.hallo.legal.framework.internal.usecase.LatestAcceptedLegalVersionUseCase
import com.project.hallo.legal.framework.internal.usecase.LatestAvailableLegalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class LegalViewModel @Inject constructor(
    private val latestAcceptedLegalVersionUseCase: LatestAcceptedLegalVersionUseCase,
    private val latestAvailableLegalUseCase: LatestAvailableLegalUseCase
) : ViewModel() {

    init {

    }

    override fun onCleared() {

    }
}