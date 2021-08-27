package com.project.hallo.legal.framework.internal.usecase.implementation

import com.project.hallo.commons.domain.data.Response
import com.project.hallo.commons.domain.data.ResponseApi
import com.project.hallo.commons.domain.data.toSuccessResponse
import com.project.hallo.legal.framework.internal.usecase.LatestAvailableLegalUseCase
import com.project.hello.legal.domain.model.LatestAvailableLegal
import com.project.hello.legal.domain.model.api.toLatestAvailableLegal
import com.project.hello.legal.domain.repository.LegalRepository
import com.project.hello.legal.domain.usecase.LatestAvailableLegalUseCaseErrorMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

internal class LatestAvailableLegalUseCaseImpl @Inject constructor(
    private val legalRepository: LegalRepository,
    private val latestAvailableLegalUseCaseErrorMapper: LatestAvailableLegalUseCaseErrorMapper
) : LatestAvailableLegalUseCase {

    override fun execute(): Observable<Response<LatestAvailableLegal>> {
        return Observable.create {
            it.onNext(Response.Loading())
            val dataResource = legalRepository.getLegalDataResource()
            val responseEvent = when (val responseApi = dataResource.latestAvailableLegal()) {
                is ResponseApi.Success -> {
                    val data = responseApi.successData
                    val latestAvailableLegal = data.toLatestAvailableLegal()
                    latestAvailableLegal.toSuccessResponse()
                }
                is ResponseApi.Error -> {
                    Response.Error<LatestAvailableLegal>(responseApi.rawErrorMessage)
                        .also { errorResponse ->
                            latestAvailableLegalUseCaseErrorMapper.mapError(errorResponse)
                        }
                }
            }
            it.onNext(responseEvent)
            it.onComplete()
        }
    }
}