package com.project.hello.legal.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.hello.commons.data.Response
import com.project.hello.commons.livedata.Event
import com.project.hello.legal.api.LatestAvailableLegalResult
import com.project.hello.legal.api.LegalViewModel
import com.project.hello.legal.internal.usecase.LatestAcceptedLegalVersionUseCase
import com.project.hello.legal.internal.usecase.LatestAvailableLegalSaverUseCase
import com.project.hello.legal.internal.usecase.LatestAvailableLegalUseCase
import com.project.hello.legal.model.LatestAvailableLegal
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

private class LatestLegalNotAcceptedException(
    localisedErrorMessage: String
) : IllegalStateException(localisedErrorMessage)

private class LatestLegalNotAvailableException(
    localisedErrorMessage: String
) : IllegalStateException(localisedErrorMessage)

@HiltViewModel
internal class LegalViewModelImpl @Inject constructor(
    private val latestAcceptedLegalVersionUseCase: LatestAcceptedLegalVersionUseCase,
    private val latestAvailableLegalUseCase: LatestAvailableLegalUseCase,
    private val latestAvailableLegalSaverUseCase: LatestAvailableLegalSaverUseCase
) : ViewModel(), LegalViewModel {

    private val disposable = CompositeDisposable()

    override val isLatestAvailableLegalAccepted = MutableLiveData<Event<Boolean>>()
    override val latestAvailableLegal = MutableLiveData<LatestAvailableLegal>()
    override val latestAvailableLegalSavedResult =
        MutableLiveData<Event<LatestAvailableLegalResult>>()

    init {
        updateInfoAboutAcceptedLegal()
    }

    override fun onCleared() {
        disposable.clear()
    }

    override fun onLatestAvailableLegalAccepted() {
        latestAvailableLegal.value?.let {
            disposable.add(
                latestAvailableLegalSaverUseCase.execute(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { handleSuccessfulSave() },
                        { handleFailedSave(it) }
                    )
            )
        }
    }

    private fun handleSuccessfulSave() {
        latestAvailableLegalSavedResult.value =
            Event(LatestAvailableLegalResult.Success)
        isLatestAvailableLegalAccepted.value = Event(true)
    }

    private fun handleFailedSave(error: Throwable) {
        latestAvailableLegalSavedResult.value =
            Event(LatestAvailableLegalResult.Error(error.message ?: ""))
        isLatestAvailableLegalAccepted.value = Event(false)
    }

    private fun updateInfoAboutAcceptedLegal() {
        disposable.add(
            fetchLatestAvailableLegal()
                .doOnNext { latestAvailableLegal.postValue(it.successData) }
                .flatMap { fetchLatestAcceptedLegalVersion() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { handleSuccessfulLegalResult(it) },
                    { handleFailedLegalResult(it) }
                )
        )
    }

    private fun handleSuccessfulLegalResult(response: Response.Success<Int>) {
        val latestAcceptedVersion = response.successData
        val latestAvailableLegal = latestAvailableLegal.value!!
        val isLatestAccepted = latestAcceptedVersion >= latestAvailableLegal.version
        isLatestAvailableLegalAccepted.value = Event(isLatestAccepted)
    }

    private fun handleFailedLegalResult(error: Throwable) {
        when (error) {
            is LatestLegalNotAcceptedException -> {
                isLatestAvailableLegalAccepted.value = Event(false)
            }
            is LatestLegalNotAvailableException -> {
                // todo currently it is impossible path
            }
        }
    }

    private fun fetchLatestAvailableLegal(): Observable<Response.Success<LatestAvailableLegal>> =
        latestAvailableLegalUseCase.execute()
            .flatMap { response ->
                when (response) {
                    is Response.Success -> Observable.just(response)
                    is Response.Error -> Observable.error(
                        LatestLegalNotAvailableException(response.localisedErrorMessage)
                    )
                    else -> Observable.never()
                }
            }

    private fun fetchLatestAcceptedLegalVersion(): Observable<Response.Success<Int>> =
        latestAcceptedLegalVersionUseCase.execute()
            .flatMap { response ->
                when (response) {
                    is Response.Success -> Observable.just(response)
                    is Response.Error -> Observable.error(
                        LatestLegalNotAcceptedException(response.localisedErrorMessage)
                    )
                    else -> Observable.never()
                }
            }
}