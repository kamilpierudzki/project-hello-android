package com.project.hello.legal.internal.usecase.implementation

import com.project.hello.commons.data.Response
import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.internal.usecase.LatestAcceptedLegalVersionUseCase
import com.project.hello.legal.repository.LegalRepository
import com.project.hello.legal.usecase.LatestAcceptedLegalUseCaseErrorMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

internal class LatestAcceptedLegalVersionUseCaseImpl @Inject constructor(
    private val legalRepository: LegalRepository,
    private val latestAcceptedLegalUseCaseErrorMapper: LatestAcceptedLegalUseCaseErrorMapper
) : LatestAcceptedLegalVersionUseCase {

    override fun execute(): Observable<Response<Int>> {
        return Observable.create {
            it.onNext(Response.Loading())
            val dataResource = legalRepository.getLegalDataResource()
            val response =
                when (val repositoryResponseApi = dataResource.latestAcceptedLegalVersion()) {
                    is ResponseApi.Error -> Response.Error<Int>(repositoryResponseApi.rawErrorMessage)
                        .also { error ->
                            latestAcceptedLegalUseCaseErrorMapper.mapError(error)
                        }
                    is ResponseApi.Success -> Response.Success(repositoryResponseApi.successData)
                }
            it.onNext(response)
            it.onComplete()
        }
    }
}