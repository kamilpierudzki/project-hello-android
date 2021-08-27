package com.project.hello.commons.framework.viewmodel

import javax.inject.Qualifier

/**
 * Qualifier intended to be used for injecting different ExternalViewModelProvider instances in the graph
 *
 * The 3 types from ViewModelType enum should be used as following:
 *
 * ViewModelType.ACTIVITY for injecting providers that will provide a viewModel with the Activity as store
 * owner through activity.viewModels()
 *
 * ViewModelType.FRAGMENT for injecting providers that will provide a viewModel with the Fragment as store
 * owner through fragment.viewModels()
 *
 * ViewModelType.FRAGMENT_PARENT for injecting providers that will provide a viewModel for with the parent ]
 * fragment as a store owner through fragment.viewModels({ fragment.requireParentFragment() }
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelProvider(val viewModelType: ViewModelType)

enum class ViewModelType {
    ACTIVITY, FRAGMENT, FRAGMENT_PARENT
}