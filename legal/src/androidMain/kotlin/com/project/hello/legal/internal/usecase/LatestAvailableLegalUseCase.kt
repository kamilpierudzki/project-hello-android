package com.project.hello.legal.internal.usecase

import com.project.hello.commons.data.Response
import com.project.hello.legal.model.LatestAvailableLegal
import io.reactivex.rxjava3.core.Observable

interface LatestAvailableLegalUseCase {
    fun execute(): Observable<Response<LatestAvailableLegal>>
}