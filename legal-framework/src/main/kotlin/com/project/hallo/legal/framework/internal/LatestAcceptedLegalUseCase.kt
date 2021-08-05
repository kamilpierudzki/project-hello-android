package com.project.hallo.legal.framework.internal

import io.reactivex.rxjava3.core.Single

internal class LatestAcceptedLegalUseCase {

    fun execute(): Single<Legal> {
        return Single.just(Legal(1, "", false))
    }
}