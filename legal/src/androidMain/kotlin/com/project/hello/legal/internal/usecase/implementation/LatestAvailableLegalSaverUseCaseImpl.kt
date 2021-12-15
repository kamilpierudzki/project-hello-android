package com.project.hello.legal.internal.usecase.implementation

import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.internal.usecase.LatestAvailableLegalSaverUseCase
import com.project.hello.legal.model.LatestAvailableLegal
import com.project.hello.legal.repository.LegalRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

internal class LatestAvailableLegalSaverUseCaseImpl @Inject constructor(
    private val legalRepository: LegalRepository
) : LatestAvailableLegalSaverUseCase {

    override fun execute(legalToSave: LatestAvailableLegal): Completable =
        Completable.create {
            val dataResource = legalRepository.getLegalDataResource()
            val saveResponseApi = dataResource.saveLatestAcceptedLegal(legalToSave)
            if (saveResponseApi is ResponseApi.Error) {
                it.onError(IllegalStateException(saveResponseApi.rawErrorMessage))
            } else {
                it.onComplete()
            }
        }
}