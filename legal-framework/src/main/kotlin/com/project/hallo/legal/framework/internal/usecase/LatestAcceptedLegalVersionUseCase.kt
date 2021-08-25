package com.project.hallo.legal.framework.internal.usecase

import com.project.hallo.commons.domain.data.Response
import com.project.hallo.commons.domain.data.ResponseApi
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
            val response =
                when (val repositoryResponseApi = dataResource.latestAcceptedLegalVersion()) {
                    is ResponseApi.Error -> Response.Error<Int>(repositoryResponseApi.rawErrorMessage)
                        .also {
                            errorMapper.mapError(it)
                        }
                    is ResponseApi.Success -> Response.Success(repositoryResponseApi.successData)
                }
            it.onNext(response)
            it.onComplete()
        }
    }
}