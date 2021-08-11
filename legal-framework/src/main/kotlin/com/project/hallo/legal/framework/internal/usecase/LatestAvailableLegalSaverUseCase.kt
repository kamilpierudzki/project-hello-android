package com.project.hallo.legal.framework.internal.usecase

import com.project.hallo.commons.domain.repository.Response
import com.project.hello.legal.domain.model.LatestAvailableLegal
import com.project.hello.legal.domain.repository.LegalRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

internal class LatestAvailableLegalSaverUseCase @Inject constructor(
    private val legalRepository: LegalRepository
) {

    fun execute(legalToSave: LatestAvailableLegal): Completable =
        Completable.create {
            val dataResource = legalRepository.getLegalDataResource()
            val saveResponse: Response<Unit> = dataResource.saveLatestAcceptedLegal(legalToSave)
            if (saveResponse is Response.Error) {
                it.onError(IllegalStateException(saveResponse.localisedErrorMessage))
            }
        }
}