package com.project.hallo.legal.framework.internal.usecase

import com.project.hallo.commons.domain.repository.Response
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
            val dataResource = legalRepository.getLegalDataResource()
            val responseApi: Response<LatestAvailableLegalApi> = dataResource.latestAvailableLegal()
            if (responseApi is Response.Success) {
                val data = responseApi.successData
                Response.Success(data.toLatestAvailableLegal())
            } else if (responseApi is Response.Error) {
                Response.Error<LatestAvailableLegal>(responseApi.rawErrorMessage)
                    .also {
                        errorMapper.mapError(it)
                    }
            }
        }
    }
}