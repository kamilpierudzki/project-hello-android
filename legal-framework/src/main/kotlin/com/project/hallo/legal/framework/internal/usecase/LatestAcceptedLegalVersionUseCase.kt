package com.project.hallo.legal.framework.internal.usecase

import com.project.hallo.commons.domain.data.Response
import io.reactivex.rxjava3.core.Observable

interface LatestAcceptedLegalVersionUseCase {
    fun execute(): Observable<Response<Int>>
}