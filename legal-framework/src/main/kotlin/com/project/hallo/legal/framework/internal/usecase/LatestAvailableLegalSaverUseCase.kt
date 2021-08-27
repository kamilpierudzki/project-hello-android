package com.project.hallo.legal.framework.internal.usecase

import com.project.hello.legal.domain.model.LatestAvailableLegal
import io.reactivex.rxjava3.core.Completable

interface LatestAvailableLegalSaverUseCase {
    fun execute(legalToSave: LatestAvailableLegal): Completable
}