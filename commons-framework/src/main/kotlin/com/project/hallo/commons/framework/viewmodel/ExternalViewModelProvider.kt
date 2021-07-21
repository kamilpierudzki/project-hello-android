package com.project.hallo.commons.framework.viewmodel

fun interface ExternalViewModelProvider<VM : ExternalViewModel> {
    fun provide(): VM
}

inline fun <reified VM : ExternalViewModel> externalViewModels(
    crossinline viewModelProvider: () -> ExternalViewModelProvider<VM>
) = lazy { viewModelProvider().provide() }