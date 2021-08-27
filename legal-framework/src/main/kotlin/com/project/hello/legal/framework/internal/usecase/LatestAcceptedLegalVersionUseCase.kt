package com.project.hello.legal.framework.internal.usecase

import com.project.hello.commons.domain.data.Response
import io.reactivex.rxjava3.core.Observable

interface LatestAcceptedLegalVersionUseCase {
    fun execute(): Observable<Response<Int>>
}