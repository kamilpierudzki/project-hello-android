package com.project.hallo.legal.framework.internal.usecase

import com.project.hallo.commons.domain.data.Response
import com.project.hello.legal.domain.model.LatestAvailableLegal
import io.reactivex.rxjava3.core.Observable

interface LatestAvailableLegalUseCase {
    fun execute(): Observable<Response<LatestAvailableLegal>>
}