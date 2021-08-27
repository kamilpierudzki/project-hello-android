package com.project.hello.legal.framework.internal.usecase

import com.project.hello.commons.domain.data.Response
import com.project.hello.legal.domain.model.LatestAvailableLegal
import io.reactivex.rxjava3.core.Observable

interface LatestAvailableLegalUseCase {
    fun execute(): Observable<Response<LatestAvailableLegal>>
}