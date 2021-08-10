package com.project.hallo.legal.framework.internal.usecase

import com.project.hallo.commons.domain.repository.Response
import com.project.hello.legal.domain.repository.LegalRepository
import com.project.hello.legal.domain.usecase.LatestAcceptedLegalUseCaseErrorMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

internal class LatestAcceptedLegalVersionUseCase @Inject constructor(
    private val legalRepository: LegalRepository,
    private val errorMapper: LatestAcceptedLegalUseCaseErrorMapper
) {

    fun execute(): Observable<Response<Int>> {
        return Observable.create {
            it.onNext(Response.Loading())
            val dataResource = legalRepository.getLegalDataResource()
            val repositoryResponse = dataResource.latestAcceptedLegalVersion()
            if (repositoryResponse is Response.Error) {
                errorMapper.mapError(repositoryResponse)
            }
            it.onNext(repositoryResponse)
        }
    }
}