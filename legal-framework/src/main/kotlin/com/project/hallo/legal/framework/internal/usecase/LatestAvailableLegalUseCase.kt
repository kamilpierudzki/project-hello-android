package com.project.hallo.legal.framework.internal.usecase

import com.project.hallo.commons.domain.data.Response
import com.project.hallo.commons.domain.data.ResponseApi
import com.project.hello.legal.domain.model.LatestAvailableLegal
import com.project.hello.legal.domain.model.api.LatestAvailableLegalApi
import com.project.hello.legal.domain.model.api.toLatestAvailableLegal
import com.project.hello.legal.domain.repository.LegalRepository
import com.project.hello.legal.domain.usecase.LatestAvailableLegalUseCaseErrorMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

internal class LatestAvailableLegalUseCase @Inject constructor(
    private val legalRepository: LegalRepository,
    private val errorMapper: LatestAvailableLegalUseCaseErrorMapper
) {

    fun execute(): Observable<Response<LatestAvailableLegal>> {
        return Observable.create {
            it.onNext(Response.Loading())
            val dataResource = legalRepository.getLegalDataResource()
            val responseApi: ResponseApi<LatestAvailableLegalApi> =
                dataResource.latestAvailableLegal()
            val responseEvent = when (responseApi) {
                is ResponseApi.Success -> {
                    val data = responseApi.successData
                    Response.Success(data.toLatestAvailableLegal())
                }
                is ResponseApi.Error -> {
                    Response.Error<LatestAvailableLegal>(responseApi.rawErrorMessage)
                        .also { errorResponse ->
                            errorMapper.mapError(errorResponse)
                        }
                }
            }
            it.onNext(responseEvent)
            it.onComplete()
        }
    }
}