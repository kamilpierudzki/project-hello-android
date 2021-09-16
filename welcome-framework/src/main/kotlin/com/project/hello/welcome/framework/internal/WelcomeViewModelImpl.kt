package com.project.hello.welcome.framework.internal

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hello.commons.framework.livedata.Event
import com.project.hello.welcome.framework.api.WelcomeViewModel
import com.project.hello.welcome.framework.internal.usecase.FirstLaunchSaverUseCase
import com.project.hello.welcome.framework.internal.usecase.FirstLaunchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
internal class WelcomeViewModelImpl @Inject constructor(
    private val firstLaunchUseCase: FirstLaunchUseCase,
    private val firstLaunchSaverUseCase: FirstLaunchSaverUseCase,
) : ViewModel(), WelcomeViewModel {

    override val isFirstLaunch = MutableLiveData<Event<Boolean>>()

    private val disposable = CompositeDisposable()

    init {
        checkIFFirstLaunch()
    }

    override fun onCleared() {
        disposable.clear()
    }

    override fun markFirstLaunchAccomplished() {
        disposable.add(
            firstLaunchSaverUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isFirstLaunch.value = Event(false)
                }, {
                    isFirstLaunch.value = Event(true)
                })
        )
    }

    @VisibleForTesting
    fun checkIFFirstLaunch() {
        disposable.add(
            firstLaunchUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { handleResponse(it) },
                    { handleError(it) }
                )
        )
    }

    private fun handleResponse(response: Boolean) {
        isFirstLaunch.value = Event(response)
    }

    private fun handleError(error: Throwable) {
        isFirstLaunch.value = Event(true)
    }
}