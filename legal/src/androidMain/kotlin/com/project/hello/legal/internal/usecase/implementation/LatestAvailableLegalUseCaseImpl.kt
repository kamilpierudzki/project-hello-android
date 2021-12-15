package com.project.hello.legal.internal.usecase.implementation

import com.project.hello.commons.data.Response
import com.project.hello.commons.data.ResponseApi
import com.project.hello.commons.data.toSuccessResponse
import com.project.hello.legal.internal.usecase.LatestAvailableLegalUseCase
import com.project.hello.legal.model.LatestAvailableLegal
import com.project.hello.legal.model.api.toLatestAvailableLegal
import com.project.hello.legal.repository.LegalRepository
import com.project.hello.legal.usecase.LatestAvailableLegalUseCaseErrorMapper
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