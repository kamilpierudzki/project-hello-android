package com.project.hello.legal.internal.usecase

import com.project.hello.commons.data.Response
import io.reactivex.rxjava3.core.Observable

interface LatestAcceptedLegalVersionUseCase {
    fun execute(): Observable<Response<Int>>
}